package com.example.blemeter.feature.scan.presentation

import com.juul.kable.AndroidAdvertisement

sealed interface ScanUiEvent {

    data object OnScan : ScanUiEvent

    data object OnScanCancel : ScanUiEvent

    data object OnConnectionCancel : ScanUiEvent

    data object OnConnect : ScanUiEvent

    data class OnDeviceSelect(val device: AndroidAdvertisement) : ScanUiEvent
}