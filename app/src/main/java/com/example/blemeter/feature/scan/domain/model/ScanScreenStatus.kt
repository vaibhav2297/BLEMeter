package com.example.blemeter.feature.scan.domain.model

import com.juul.kable.AndroidAdvertisement
import com.juul.kable.State

sealed interface ScanScreenStatus {

    data object None : ScanScreenStatus

    data object Scanning : ScanScreenStatus

    data class OnError(val message: String) : ScanScreenStatus

    data class OnDevicesFound(val foundDevices: List<AndroidAdvertisement>) : ScanScreenStatus

    data object NoDeviceFound : ScanScreenStatus

    data class OnConnection(val state: State) : ScanScreenStatus
}