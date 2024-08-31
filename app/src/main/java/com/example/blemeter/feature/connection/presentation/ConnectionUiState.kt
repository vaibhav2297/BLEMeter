package com.example.blemeter.feature.connection.presentation

import com.example.blemeter.core.ble.domain.model.ConnectionState
import com.example.blemeter.core.ble.domain.model.DeviceDetail
import com.example.blemeter.core.ble.domain.model.ScannedDevice
import com.juul.kable.AndroidAdvertisement
import com.juul.kable.State

data class ConnectionUiState(
    val isScanning: Boolean = false,
    val advertisements: List<AndroidAdvertisement> = listOf(),
    val state: State = State.Disconnected(),
    val connectedDevice: AndroidAdvertisement? = null,


    val scannedDevices: List<ScannedDevice> = listOf(),
    val deviceDetail: DeviceDetail? = null,
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED,
    val connectionError: String? = null,
    val isBluetoothEnabled: Boolean = true,
    val isMeterAddressRequired: Boolean = false,

    //bluetooth permissions
    val showBluetoothEnableDialog: Boolean = false,
    val shouldRequestBluetoothPermission: Boolean = false,
    val isPermissionGranted: Boolean = false,

    val dialog: ConnectionUiDialog = ConnectionUiDialog.None,
    val isServiceDiscovered: Boolean = false
)
