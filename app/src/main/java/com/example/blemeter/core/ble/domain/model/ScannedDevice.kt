package com.example.blemeter.core.ble.domain.model

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.os.ParcelUuid

data class ScannedDevice(
    val deviceName: String,
    val address: String,
    val services: List<ParcelUuid>
)

@SuppressLint("MissingPermission")
fun ScanResult.toScannedDevice() =
    ScannedDevice(
        deviceName = device.name ?: "",
        address = device.address ?: "",
        services = this.device?.uuids?.toList() ?: listOf(),
    )

@SuppressLint("MissingPermission")
fun BluetoothDevice.toScannedDevice() =
    ScannedDevice(
        deviceName = name ?: "",
        address = address ?: "",
        services = uuids?.toList() ?: listOf(),
    )
