package com.example.blemeter.feature.connection.presentation

import com.example.blemeter.core.ble.domain.model.ScannedDevice

sealed interface ConnectionUiEvent {

    data class OnConnectionEstablish(
        val device: ScannedDevice
    ) : ConnectionUiEvent

    data object OnStartScan : ConnectionUiEvent

    data object OnStopScan : ConnectionUiEvent

    /**
     * Event is used for Bluetooth operations
     * */
    sealed interface BluetoothEvent : ConnectionUiEvent {
        data class OnPermissionResult(val isGranted: Boolean) : BluetoothEvent
        data object OnBluetoothEnableDismiss : BluetoothEvent
        data object OnBluetoothEnabled : BluetoothEvent
    }
}