package com.example.blemeter.feature.scan.presentation

import com.juul.kable.AndroidAdvertisement

sealed interface ScanUiEvent {

    data object OnScan : ScanUiEvent

    data object OnScanCancel : ScanUiEvent

    data object OnConnectionCancel : ScanUiEvent

    data object OnConnect : ScanUiEvent

    data class OnDeviceSelect(val device: AndroidAdvertisement) : ScanUiEvent

    data object OnNavigated : ScanUiEvent

    data class OnPermissionResult(val isGranted: Boolean) : ScanUiEvent

    data class OnBluetoothEnabled(val isEnabled: Boolean) : ScanUiEvent
}