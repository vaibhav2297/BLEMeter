package com.example.blemeter.feature.dashboard.domain.usecases

import com.example.blemeter.config.model.MeterConfig
import com.example.blemeter.core.ble.domain.command.AccumulateCommand
import com.example.blemeter.core.ble.domain.model.request.AccumulateDataRequest
import com.example.blemeter.core.local.DataStore
import com.example.blemeter.feature.dashboard.domain.repository.IDashboardRepository
import com.example.local.datastore.DataStoreKeys
import com.example.local.datastore.IAppDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AccumulateDataUseCase @Inject constructor(
    private val repository: IDashboardRepository,
    private val dataStore: IAppDataStore
) {
    @OptIn(ExperimentalUnsignedTypes::class)
    suspend operator fun invoke(request: AccumulateDataRequest) : Result<Boolean> {
        return try {

            val meterAddress = dataStore.getPreference(DataStoreKeys.METER_ADDRESS_KEY, "").first()

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