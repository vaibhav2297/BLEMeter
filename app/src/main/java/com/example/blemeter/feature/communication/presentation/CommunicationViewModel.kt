package com.example.blemeter.feature.communication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blemeter.config.isConnected
import com.example.blemeter.core.ble.data.repository.BLEService
import com.example.blemeter.core.ble.data.repository.IBLERepository
import com.example.blemeter.core.ble.domain.bleparsable.MeterDataCommand
import com.example.blemeter.core.ble.domain.model.DeviceDetail
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.example.blemeter.core.ble.domain.model.hasConfigCharacteristic
import com.example.blemeter.core.ble.domain.model.isConnected
import com.example.blemeter.core.ble.domain.model.request.MeterDataRequest
import com.example.blemeter.core.ble.domain.model.request.ValveControlCommandStatus
import com.example.blemeter.core.ble.utils.toHexString
import com.example.blemeter.core.logger.ILogger
import com.example.blemeter.feature.communication.domain.usecases.CommunicationUseCases
import com.example.blemeter.model.Data
import com.example.blemeter.model.MeterData
import com.example.blemeter.model.ValveControlData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunicationViewModel @Inject constructor(
    private val bleRepository: IBLERepository,
    private val bleService: BLEService,
    private val logger: ILogger,
    private val useCases: CommunicationUseCases
) : ViewModel() {

    companion object {
        private const val POLLING_TIME = 1500L
    }

    private var _pollingJob: Job? = null
    private var _observeMeterReadDataJob: Job? = null

    private val MAX_COUNT = 5
    private var retryAttempt = 0

    private val _uiState = MutableStateFlow(CommunicationUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeConnectionState()
        observeMeterData()
        startPollingMeterData()
    }

    fun onEvent(event: CommunicationUiEvent) {
        when (event) {
            is CommunicationUiEvent.OnMeterDataRead -> startPollingMeterData()
            is CommunicationUiEvent.OnValveInteraction -> valveInteraction(event.valveControlCommandStatus)
        }
    }

    private fun observeConnectionState() {
        viewModelScope.launch {
            bleService.peripheral?.state?.collectLatest { state ->
                logger.d("status: $state")
                _uiState.update { it.copy(connectionState = state) }
            }
        }
    }

    //region Meter Data
    private suspend fun readMeterData() {
        if (_uiState.value.connectionState.isConnected()) {
            logger.d("Read meter data...")
            useCases.readMeterDataUseCase(MeterDataRequest())
                .onFailure { e ->
                    logger.d("readMeterData :: Failure :: ${e.message}")
                    stopPollingMeterData()
                }
        } else {
            logger.d("readMeterData :: Device is not connected")
            stopPollingMeterData()
        }
    }

    private fun observeMeterData() {
        _observeMeterReadDataJob = viewModelScope.launch {
            bleService.observeCharacteristic(
                service = MeterServicesProvider.MainService.SERVICE,
                observeCharacteristic = MeterServicesProvider.MainService.NOTIFY_CHARACTERISTIC
            )?.map { response ->

                logger.d("observeMeterData :: Response :: ${response.contentToString()}")
                logger.d("observeMeterData :: Response :: ${response.toHexString()}")

                val meterData = MeterDataCommand.fromCommand(response)
                logger.d("observeMeterData :: Response to MeterData :: $meterData")

                meterData
            }?.catch {e ->
                logger.d("observeMeterData :: catch :: ${e.message}")
                retryAttempt++
                logger.d("observeMeterData :: Retrying attempt : $retryAttempt")
                if (retryAttempt == MAX_COUNT) {
                    stopPollingMeterData()
                }
            }?.collect { meterData ->
                //handle meter data
                _uiState.update {
                    it.copy(meterData = meterData)
                }
            }
        }
    }

    private fun startPollingMeterData() {
        logger.d("Start polling Read meter data....")
        _pollingJob = viewModelScope.launch {
            while (isActive) {
                readMeterData()
                delay(POLLING_TIME)
            }
        }
    }

    private fun stopPollingMeterData() {
        logger.e("stopPollingMeterData")
        _pollingJob?.cancel()
        retryAttempt = 0
    }
    //endregion Meter Data

    private fun valveInteraction(status: ValveControlCommandStatus) {
        viewModelScope.launch {
            if (bleRepository.connectionState.value.isConnected()) {
                logger.d("valve interaction : ${status.name}")
                _uiState.update { it.copy(isLoading = true) }
                useCases.valveControlUseCase(status)
            }
        }
    }

    override fun onCleared() {
        stopPollingMeterData()
        _observeMeterReadDataJob?.cancel()
        _observeMeterReadDataJob = null
        super.onCleared()
    }
}