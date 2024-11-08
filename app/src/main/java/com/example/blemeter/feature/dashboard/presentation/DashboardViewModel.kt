package com.example.blemeter.feature.dashboard.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blemeter.config.model.CalibrationIdentification
import com.example.blemeter.config.model.InPlaceMethod
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.example.blemeter.core.local.DataStore
import com.example.blemeter.feature.dashboard.domain.model.MeterControl
import com.example.blemeter.feature.dashboard.domain.usecases.DashboardUseCases
import com.example.blemeter.feature.recharge.navigation.RechargeDestination
import com.example.blemeter.feature.valvecontrol.navigation.ValveControlDestination
import com.example.blemeter.config.model.MeterData
import com.example.blemeter.config.model.MeterType
import com.example.blemeter.config.model.NoData
import com.example.blemeter.config.model.PaymentMethod
import com.example.blemeter.core.ble.domain.model.request.AccumulateDataRequest
import com.example.blemeter.core.ble.domain.model.request.NumberingInstructionDataRequest
import com.example.blemeter.feature.dashboard.domain.usecases.ObserveDataUseCase
import com.example.designsystem.utils.ScreenState
import com.example.local.datastore.DataStoreKeys
import com.example.local.datastore.IAppDataStore
import com.example.navigation.BLEMeterNavDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val useCases: DashboardUseCases,
    private val observeDataUseCase: ObserveDataUseCase,
    private val dataStore: IAppDataStore
) : ViewModel() {

    companion object {
        const val TAG = "DashboardViewModel"
    }

    private val _uiState: MutableStateFlow<DashboardUiState> by lazy {
        MutableStateFlow(DashboardUiState())
    }
    val uiState = _uiState.asStateFlow()

    init {
        observeResponse()
        //readMeterData()
    }

    fun onEvent(event: DashboardUiEvent) {
        when (event) {
            is DashboardUiEvent.OnMeterControl -> onMeterControl(event.control)
            is DashboardUiEvent.OnNavigated -> onNavigateTo(null)
            is DashboardUiEvent.OnRefresh -> readMeterData()
        }
    }

    private fun onMeterControl(control: MeterControl) {
        when (control) {
            MeterControl.READ_DATA -> readMeterData()
            MeterControl.VALVE_CONTROL -> onNavigateTo(ValveControlDestination)
            MeterControl.RECHARGE -> onNavigateTo(RechargeDestination)
            MeterControl.RESET_DATA -> resetMeterData()
            MeterControl.ACCUMULATE -> accumulateUsage()
            MeterControl.NUMBERING_INSTRUCTION -> numberingInstructionData()
        }
    }

    private fun resetMeterData() {
        viewModelScope.launch {
            showLoading()
            useCases.zeroInitialisationUseCase()
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            screenState = ScreenState.Error(
                                e.message ?: "unknown error"
                            )
                        )
                    }
                }
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            screenState = ScreenState.Success(Unit)
                        )
                    }
                }
        }
    }

    private fun readMeterData() {
        viewModelScope.launch {
            showLoading()
            useCases.readMeterDataUseCase()
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            screenState = ScreenState.Error(
                                e.message ?: "unknown error"
                            )
                        )
                    }
                }
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            screenState = ScreenState.Success(Unit)
                        )
                    }
                }
        }
    }

    private fun accumulateUsage() {
        viewModelScope.launch {
            showLoading()
            useCases.accumulateDataUseCase(
                request = AccumulateDataRequest(100u)
            )
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            screenState = ScreenState.Error(
                                e.message ?: "unknown error"
                            )
                        )
                    }
                }
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            screenState = ScreenState.Success(Unit)
                        )
                    }
                }
        }
    }

    private fun numberingInstructionData() {
        viewModelScope.launch {
            showLoading()

            val request = NumberingInstructionDataRequest(
                calibrationIdentification = CalibrationIdentification.HUNDRED_LITRE,
                inPlaceMethod = InPlaceMethod.FIVE_WIRE_ACTUATOR,
                paymentMethod = PaymentMethod.STEP
            )

            useCases.numberingInstructionDataUseCase(request = request)
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            screenState = ScreenState.Error(
                                e.message ?: "unknown error"
                            )
                        )
                    }
                }
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            screenState = ScreenState.Success(Unit)
                        )
                    }
                }
        }
    }

    private fun observeResponse() {
        viewModelScope.launch {
            observeDataUseCase(
                service = MeterServicesProvider.MainService.SERVICE,
                observeCharacteristic = MeterServicesProvider.MainService.NOTIFY_CHARACTERISTIC
            )?.catch { cause ->

            }?.collect { data ->
                Log.e(TAG, "Dashboard observerResponse :: $data")
                when (data) {
                    is MeterData -> {
                        _uiState.update { it.copy(meterData = data) }

                        //saving recharge times
                        saveRechargeTimes(data.numberTimes.toInt())

                        //saving the meter calibration
                        saveMeterCalibrationType(data.productVersion.calibrationIdentification)
                    }

                    is NoData -> {
                        _uiState.update {
                            it.copy(
                                screenState = ScreenState.Error("No Data received from device")
                            )
                        }
                    }
                }

                //updating sync time on every successful response
                updateSyncTime()
            }
        }
    }

    private fun updateSyncTime() {
        _uiState.update {
            it.copy(lastSync = System.currentTimeMillis())
        }
    }

    private suspend fun saveRechargeTimes(numberOfTimes: Int) {
        dataStore.putPreference(DataStoreKeys.RECHARGE_TIMES_KEY, numberOfTimes)
    }

    private suspend fun saveMeterCalibrationType(identification: CalibrationIdentification) {
        dataStore.putPreference(DataStoreKeys.METER_CALIBRATION_TYPE, identification.commandBit.toInt())
    }

    private fun onNavigateTo(destination: BLEMeterNavDestination?) {
        _uiState.update {
            it.copy(navigationTo = destination)
        }
    }

    private fun showLoading() {
        _uiState.update {
            it.copy(
                screenState = ScreenState.Loading
            )
        }
    }
}