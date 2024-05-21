package com.example.blemeter.core.ble.domain.repository

import com.example.blemeter.core.ble.data.IBLEGATTService
import com.example.blemeter.core.ble.data.IBLEScanService
import com.example.blemeter.core.ble.data.repository.IBLERepository
import com.example.blemeter.core.ble.domain.model.BleScanEvent
import com.example.blemeter.core.ble.domain.model.ConnectionState
import com.example.blemeter.core.ble.domain.model.DeviceService
import com.example.blemeter.core.ble.domain.model.ScannedDevice
import com.example.blemeter.model.Data
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class BLERepository @Inject constructor(
    private val bleGattService: IBLEGATTService,
    private val bleScanService: IBLEScanService
) : IBLERepository {

    override val bleScanEvent : StateFlow<BleScanEvent>  = bleScanService.scanEvent

    override val connectionState : StateFlow<ConnectionState> = bleGattService.connectionState

    override val deviceServices : StateFlow<List<DeviceService>> = bleGattService.deviceServices

    override val connectedDevice : StateFlow<ScannedDevice?> = bleGattService.connectedDevice

    override val data : StateFlow<Data?> = bleGattService.data

    override suspend fun scanLeDevice() {
        bleScanService.scanLeDevice()
    }

    override fun stopLeScan() {
        bleScanService.stopLeScan()
    }

    override fun connect(scannedDevice: ScannedDevice) {
        bleGattService.connectToDevice(scannedDevice)
    }

    override fun close() {
        bleGattService.close()
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    override fun writeBytes(uuid: String, values: UByteArray) {
        bleGattService.writeBytes(uuid, values.toByteArray())
    }
}