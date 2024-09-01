package com.example.blemeter.feature.scan.presentation

import com.example.blemeter.feature.scan.domain.model.ScanScreenStatus
import com.example.blemeter.navigation.BLEMeterNavDestination
import com.juul.kable.AndroidAdvertisement

data class ScanUiState(
    val isScanning: Boolean = false,
    val selectedDevice: AndroidAdvertisement? = null,
    val foundDevices: List<AndroidAdvertisement> = listOf(),
    val screenStatus: ScanScreenStatus = ScanScreenStatus.None,
    val navigateTo: BLEMeterNavDestination? = null
)
