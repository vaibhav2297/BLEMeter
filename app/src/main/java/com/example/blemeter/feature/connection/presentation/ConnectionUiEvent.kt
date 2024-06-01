package com.example.blemeter.feature.connection.presentation

import com.example.blemeter.core.ble.domain.model.ScannedDevice
import com.juul.kable.AndroidAdvertisement

sealed interface ConnectionUiEvent {

    data class OnConnectionEstablish(
        val device: AndroidAdvertisement
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

    data object OnNavigated : ConnectionUiEvent
}