package com.example.blemeter.core.ble.data.repository

import com.example.blemeter.core.ble.domain.model.BleScanEvent
import com.example.blemeter.core.ble.domain.model.ConnectionState
import com.example.blemeter.core.ble.domain.model.DeviceService
import com.example.blemeter.core.ble.domain.model.ScannedDevice
import com.example.blemeter.model.Data
import kotlinx.coroutines.flow.StateFlow

interface IBLERepository {

    val bleScanEvent: StateFlow<BleScanEvent>
    val connectionState: StateFlow<ConnectionState>
    val deviceServices: StateFlow<List<DeviceService>>
    val connectedDevice: StateFlow<ScannedDevice?>
    val data: StateFlow<Data?>
    suspend fun scanLeDevice()
    fun stopLeScan()
    fun connect(scannedDevice: ScannedDevice)
    fun close()
    @OptIn(ExperimentalUnsignedTypes::class)
    fun writeBytes(uuid: String, values: UByteArray)
}