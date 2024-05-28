package com.example.blemeter.feature.communication.presentation

import com.example.blemeter.core.ble.domain.model.ConnectionState
import com.example.blemeter.core.ble.domain.model.ScannedDevice
import com.example.blemeter.model.MeterData
import com.example.blemeter.model.ValveControlData

data class CommunicationUiState(
    val isLoading: Boolean = false,
    val isReadingMeterData: Boolean = false,
    val error: String? = null,
    val meterData: MeterData = MeterData(),
    val valveControlData: ValveControlData = ValveControlData(),
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED,
    val connectedDevice: ScannedDevice? = null
)
