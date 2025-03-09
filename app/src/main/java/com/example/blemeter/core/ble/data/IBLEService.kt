package com.example.blemeter.core.ble.data

import com.juul.kable.AndroidAdvertisement
import com.juul.kable.Peripheral
import com.juul.kable.PlatformScanner
import kotlinx.coroutines.flow.Flow

interface IBLEService {

    val scanner: PlatformScanner

    fun initPeripheral(advertisement: AndroidAdvertisement) : Peripheral?

    suspend fun connect()

    suspend fun disconnect()

    suspend fun readCharacteristics(service: String, characteristic: String): ByteArray?

    @OptIn(ExperimentalUnsignedTypes::class)
    suspend fun writeCharacteristics(
        service: String,
        writeCharacteristic: String,
        value: UByteArray
    )

    @OptIn(ExperimentalUnsignedTypes::class)
    fun observeCharacteristic(service: String, observeCharacteristic: String): Flow<UByteArray>?

    suspend fun connectAndWrite(onCharWrite: suspend () -> Unit)
}