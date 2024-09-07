package com.example.blemeter.feature.dashboard.data.repository

import com.example.blemeter.core.ble.data.IBLEService
import com.example.blemeter.feature.dashboard.domain.repository.IDashboardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OptIn(ExperimentalUnsignedTypes::class)
class DashboardRepository @Inject constructor(
    private val bleService: IBLEService
) : IDashboardRepository {

    override suspend fun connectAndWrite(
        service: String,
        writeCharacteristic: String,
        value: UByteArray
    ) {
        bleService.connectAndWrite {
            bleService.writeCharacteristics(
                service = service,
                writeCharacteristic = writeCharacteristic,
                value = value
            )
        }
    }

    override suspend fun readCharacteristics(
        service: String,
        characteristic: String
    ): ByteArray? =
        bleService.readCharacteristics(
            service = service,
            characteristic = characteristic
        )

    override fun observeCharacteristic(
        service: String,
        observeCharacteristic: String
    ): Flow<UByteArray>? =
        bleService.observeCharacteristic(
            service = service,
            observeCharacteristic = observeCharacteristic
        )
}