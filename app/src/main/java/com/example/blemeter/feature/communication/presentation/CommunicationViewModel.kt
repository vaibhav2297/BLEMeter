package com.example.blemeter.feature.communication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blemeter.core.ble.data.repository.IBLERepository
import com.example.blemeter.core.ble.domain.bleparsable.Command
import com.example.blemeter.core.ble.domain.bleparsable.MeterDataCommand
import com.example.blemeter.core.ble.domain.bleparsable.ValveControlCommand
import com.example.blemeter.core.ble.domain.model.request.MeterDataRequest
import com.example.blemeter.core.ble.domain.model.request.ValveControlCommandStatus
import com.example.blemeter.core.ble.domain.model.request.ValveControlRequest
import com.example.blemeter.core.ble.utils.BLEConstants
import com.example.blemeter.core.logger.ILogger
import com.example.blemeter.model.MeterData
import com.example.blemeter.model.ValveControlData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalUnsignedTypes::class)
@HiltViewModel
class CommunicationViewModel @Inject constructor(
    private val bleRepository: IBLERepository,
    private val logger: ILogger
) : ViewModel() {

    private val _connectedDevice = bleRepository.connectedDevice
    private val _connectionState = bleRepository.connectionState
    private val _data = bleRepository.data
    private val _services = bleRepository.deviceServices

    private val _uiState = MutableStateFlow(CommunicationUiState())

    val uiState = combine(
        _data,
        _connectionState,
        _connectedDevice
    ) { data, connectionState, device ->

        data?.let {
            when (data) {
                is MeterData -> {
                    _uiState.update {
                        it.copy(
                            meterData = data,
                            isCommunicating = false
                        )
                    }
                }

                is ValveControlData -> {
                    _uiState.update {
                        it.copy(
                            valveControlData = data,
                            isCommunicating = false
                        )
                    }
                }
            }
        }

        CommunicationUiState(
            meterData = _uiState.value.meterData,
            valveControlData = _uiState.value.valveControlData,
            isCommunicating = _uiState.value.isCommunicating,
            connectionState = connectionState,
            connectedDevice = device
        )

    }.flowOn(Dispatchers.IO)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            CommunicationUiState()
        )

    fun onEvent(event: CommunicationUiEvent) {
        when (event) {
            is CommunicationUiEvent.OnMeterDataRead -> readMeterData()
            is CommunicationUiEvent.OnValveInteraction -> valveInteraction(event.valveControlCommandStatus)
        }
    }

    private fun readMeterData() {
        viewModelScope.launch {
            logger.d("read meter data")
            _uiState.update { it.copy(isCommunicating = true) }
            writeCharacteristic(MeterDataCommand, MeterDataRequest())
        }
    }

    private fun valveInteraction(status: ValveControlCommandStatus) {
        viewModelScope.launch {
            logger.d("valve interaction")
            _uiState.update { it.copy(isCommunicating = true) }
            writeCharacteristic(ValveControlCommand, ValveControlRequest(status))
        }
    }

    private fun <T> writeCharacteristic(command: Command<T, *>, request: T) = run {
        _services.value.let { svc ->
            svc.flatMap { it.characteristics }.find {
                it.uuid == BLEConstants.sendId
            }?.run {
                bleRepository.writeBytes(uuid, command.toCommand(request))
            }
        }
    }

}