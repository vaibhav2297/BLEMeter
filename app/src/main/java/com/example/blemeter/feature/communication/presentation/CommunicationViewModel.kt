package com.example.blemeter.feature.communication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blemeter.core.ble.data.repository.IBLERepository
import com.example.blemeter.core.ble.domain.model.DeviceDetail
import com.example.blemeter.core.ble.domain.model.MeterServicesProvider
import com.example.blemeter.core.ble.domain.model.hasConfigCharacteristic
import com.example.blemeter.core.ble.domain.model.isConnected
import com.example.blemeter.core.ble.domain.model.request.MeterDataRequest
import com.example.blemeter.core.ble.domain.model.request.ValveControlCommandStatus
import com.example.blemeter.core.logger.ILogger
import com.example.blemeter.feature.communication.domain.usecases.CommunicationUseCases
import com.example.blemeter.model.Data
import com.example.blemeter.model.MeterData
import com.example.blemeter.model.ValveControlData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunicationViewModel @Inject constructor(
    private val bleRepository: IBLERepository,
    private val logger: ILogger,
    private val useCases: CommunicationUseCases
) : ViewModel() {

    private val _services = bleRepository.deviceServices
    private val _device = bleRepository.connectedDevice
    private val _connectionState = bleRepository.connectionState

    private val _deviceDetails =
        combine(
            _device,
            _services,
            _connectionState
        ) { selectedDevice, deviceServices, connectionState ->
            selectedDevice?.let {
                DeviceDetail(
                    device = it,
                    services = deviceServices,
                    connectionState = connectionState,
                    isSupportConfig = deviceServices.hasConfigCharacteristic(MeterServicesProvider.MainService.CLIENT_CHARACTERISTIC_CONFIGURATION_DESCRIPTOR)
                )
            }
        }

    private val _uiState = MutableStateFlow(CommunicationUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeConnectedDevice()
        observeConnectionState()
        observeReadPollingStatus()
        observeBLEData()
        startReadingMeterData()
    }

    fun onEvent(event: CommunicationUiEvent) {
        when (event) {
            is CommunicationUiEvent.OnMeterDataRead -> startReadingMeterData()
            is CommunicationUiEvent.OnValveInteraction -> valveInteraction(event.valveControlCommandStatus)
        }
    }

    private fun observeConnectedDevice() {
        viewModelScope.launch {
            bleRepository.connectedDevice.collectLatest { device ->
                logger.d("device: $device")
                _uiState.update { it.copy(connectedDevice = device) }
            }
        }
    }

    private fun observeConnectionState() {
        viewModelScope.launch {
            bleRepository.connectionState.collectLatest { state ->
                logger.d("status: $state")
                _uiState.update { it.copy(connectionState = state) }
            }
        }
    }

    private fun observeBLEData() {
        viewModelScope.launch {
            bleRepository.data.collectLatest { result ->
                result.onSuccess { data ->
                    //setting error as null if it is success
                    _uiState.update { it.copy(error = null) }
                    onDataReceived(data)
                }.onFailure {
                    onFailure(it.message ?: "Unknown error")
                }
            }
        }
    }

    private fun getDeviceInfo() {
        viewModelScope.launch {
            useCases.getDeviceInfoUseCase()
        }
    }

    private fun onFailure(error: String) {
        _uiState.update {
            it.copy(
                error = error,
                isLoading = false,
                isReadingMeterData = false
            )
        }
    }

    private fun onDataReceived(data: Data?) {
        data?.let {
            when (data) {
                is MeterData -> {
                    _uiState.update {
                        it.copy(meterData = data)
                    }
                }

                is ValveControlData -> {
                    _uiState.update {
                        it.copy(valveControlData = data, isLoading = false)
                    }
                }
            }
        }
    }

    //region Meter Data
    private fun startReadingMeterData() {
        viewModelScope.launch {
            if (bleRepository.connectionState.value.isConnected()) {
                logger.d("start read meter data")
                //indicating the read meter polling has been started
                //Reading data is a continuous polling hence not showing loading indicator
                _uiState.update { it.copy(isReadingMeterData = true) }
                useCases.readMeterDataUseCase.startMeterDataPolling(MeterDataRequest())
            }
        }
    }

    private fun observeReadPollingStatus() {
        viewModelScope.launch {
            useCases.readMeterDataUseCase.isPolling.collectLatest { isPolling ->
                _uiState.update { it.copy(isReadingMeterData = isPolling) }
            }
        }
    }

    private fun stopReadingMeterData() {
        viewModelScope.launch {
            logger.d("stop reading meter data")
            useCases.readMeterDataUseCase.stopMeterDataPolling()
        }
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
        stopReadingMeterData()
        super.onCleared()
    }
}