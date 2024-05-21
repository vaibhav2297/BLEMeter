package com.example.blemeter.feature.connection.presentation

import com.example.blemeter.core.ble.domain.model.ConnectionState
import com.example.blemeter.core.ble.domain.model.DeviceDetail
import com.example.blemeter.core.ble.domain.model.ScannedDevice

data class ConnectionUiState(
    val isScanning: Boolean = false,
    val scannedDevices: List<ScannedDevice> = listOf(),
    val deviceDetail: DeviceDetail? = null,
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED,
    val connectionError: String? = null,
    val isBluetoothEnabled: Boolean = true,

    //bluetooth permissions
    val showBluetoothEnableDialog: Boolean = false,
    val shouldRequestBluetoothPermission: Boolean = false,
    val isPermissionGranted: Boolean = false,

    val dialog: ConnectionUiDialog = ConnectionUiDialog.None
)
