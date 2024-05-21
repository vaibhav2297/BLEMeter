package com.example.blemeter.core.ble.data

import com.example.blemeter.core.ble.domain.model.ConnectionState
import com.example.blemeter.core.ble.domain.model.DeviceService
import com.example.blemeter.core.ble.domain.model.ScannedDevice
import com.example.blemeter.model.Data
import kotlinx.coroutines.flow.StateFlow

interface IBLEGATTService {

    val connectionState: StateFlow<ConnectionState>
    val deviceServices: StateFlow<List<DeviceService>>
    fun connectToDevice(scannedDevice: ScannedDevice)
    fun close()
    fun readCharacteristics(uuid: String)
    fun writeBytes(uuid: String, bytes: ByteArray)
    val connectedDevice: StateFlow<ScannedDevice?>
    val data: StateFlow<Data?>
}