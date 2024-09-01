package com.example.blemeter.feature.dashboard.domain.usecases

import com.example.blemeter.config.model.MeterConfig
import com.example.blemeter.core.ble.domain.bleparsable.AccumulateCommand
import com.example.blemeter.core.ble.domain.model.request.AccumulateDataRequest
import com.example.blemeter.core.local.DataStore
import com.example.blemeter.feature.dashboard.domain.repository.IDashboardRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AccumulateDataUseCase @Inject constructor(
    private val repository: IDashboardRepository,
    private val dataStore: DataStore
) {
    @OptIn(ExperimentalUnsignedTypes::class)
    suspend operator fun invoke(request: AccumulateDataRequest) : Result<Boolean> {
        return try {

            val meterAddress = dataStore.getMeterAddress().first()

            val accumulationCommand = AccumulateCommand.toCommand(
                request = request,
                meterConfig = MeterConfig(meterAddress = meterAddress)
            )

            repository.connectAndWrite(
                service = AccumulateCommand.serviceUuid,
                writeCharacteristic = AccumulateCommand.characteristicUuid,
                value = accumulationCommand
            )

            Result.success(true)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}