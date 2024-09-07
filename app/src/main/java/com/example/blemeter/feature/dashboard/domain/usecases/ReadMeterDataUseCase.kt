package com.example.blemeter.feature.dashboard.domain.usecases

import com.example.blemeter.config.model.MeterConfig
import com.example.blemeter.core.ble.domain.command.ReadMeterDataCommand
import com.example.blemeter.core.ble.domain.model.request.MeterDataRequest
import com.example.blemeter.core.local.DataStore
import com.example.blemeter.feature.dashboard.domain.repository.IDashboardRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ReadMeterDataUseCase @Inject constructor(
    private val repository: IDashboardRepository,
    private val dataStore: DataStore
) {
    @OptIn(ExperimentalUnsignedTypes::class)
    suspend operator fun invoke(): Result<Boolean> {
        return try {

            val meterAddress = dataStore.getMeterAddress().first()

            val meterCommand = ReadMeterDataCommand.toCommand(
                request = MeterDataRequest(),
                meterConfig = MeterConfig(meterAddress = meterAddress)
            )

            repository.connectAndWrite(
                service = ReadMeterDataCommand.serviceUuid,
                writeCharacteristic = ReadMeterDataCommand.characteristicUuid,
                value = meterCommand
            )

            Result.success(true)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}