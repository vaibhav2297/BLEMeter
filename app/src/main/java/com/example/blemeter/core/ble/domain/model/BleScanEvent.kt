package com.example.blemeter.core.ble.domain.model

data class BleScanEvent(
    val isScanning: Boolean = false,
    val scannedDevices: List<ScannedDevice> = listOf(),
    val error: String? = null,
    val isBluetoothEnabled:  Boolean = true
)