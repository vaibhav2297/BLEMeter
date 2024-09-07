package com.example.blemeter.feature.dashboard.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.example.blemeter.core.local.DataStore
import com.example.blemeter.feature.dashboard.domain.model.MeterControl
import com.example.blemeter.feature.dashboard.domain.usecases.DashboardUseCases
import com.example.blemeter.feature.recharge.navigation.RechargeDestination
import com.example.blemeter.feature.valvecontrol.navigation.ValveControlDestination
import com.example.blemeter.model.MeterData
import com.example.blemeter.navigation.BLEMeterNavDestination
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
    private val dataStore: DataStore
) : ViewModel() {

    companion object {
        const val TAG = "DashboardViewModel"
    }

    init {
        observeResponse()
        readMeterData()
    }

    private val _uiState: MutableStateFlow<DashboardUiState> by lazy {
        MutableStateFlow(DashboardUiState())
    }
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: DashboardUiEvent) {
        when (event) {
            is DashboardUiEvent.OnMeterControl -> onMeterControl(event.control)
            is DashboardUiEvent.OnNavigated -> onNavigateTo(null)
        }
    }

    private fun onMeterControl(control: MeterControl) {
        when (control) {
            MeterControl.READ_DATA -> readMeterData()
            MeterControl.VALVE_CONTROL -> onNavigateTo(ValveControlDestination)
            MeterControl.RECHARGE -> onNavigateTo(RechargeDestination)
            MeterControl.RESET_DATA -> resetMeterData()
            MeterControl.ACCUMULATE -> { }
        }
    }

    private fun resetMeterData() {
        viewModelScope.launch {
            useCases.zeroInitialisationUseCase()
                .onFailure { }
                .onSuccess { }
        }
    }

    private fun readMeterData() {
        viewModelScope.launch {
            useCases.readMeterDataUseCase()
                .onFailure { }
                .onSuccess { }
        }
    }

    private fun observeResponse() {
        viewModelScope.launch {
            useCases.observeDataUseCase(
                service = MeterServicesProvider.MainService.SERVICE,
                observeCharacteristic = MeterServicesProvider.MainService.NOTIFY_CHARACTERISTIC
            )?.catch { cause ->

            }?.collect { data ->
                Log.e(TAG, "observerResponse :: $data")
                when (data) {
                    is MeterData -> {
                        _uiState.update { it.copy(meterData = data) }

                        //saving recharge times
                        saveRechargeTimes(data.numberTimes.toInt())
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

    private fun saveRechargeTimes(numberOfTimes: Int) {
        viewModelScope.launch {
            dataStore.saveRechargeTimes(numberOfTimes)
        }
    }

    private fun onNavigateTo(destination: BLEMeterNavDestination?) {
        _uiState.update {
            it.copy(navigationTo = destination)
        }
    }
}