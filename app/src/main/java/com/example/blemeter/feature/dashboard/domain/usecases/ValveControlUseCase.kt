package com.example.blemeter.feature.dashboard.domain.usecases

import com.example.blemeter.config.model.MeterConfig
import com.example.blemeter.core.ble.domain.command.ValveControlCommand
import com.example.blemeter.core.ble.domain.model.request.ValveControlRequest
import com.example.blemeter.core.ble.domain.model.request.ValveInteractionCommand
import com.example.blemeter.core.local.DataStore
import com.example.blemeter.feature.dashboard.domain.repository.IDashboardRepository
import com.example.local.datastore.DataStoreKeys
import com.example.local.datastore.IAppDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@OptIn(ExperimentalUnsignedTypes::class)
class ValveControlUseCase @Inject constructor(
    private val repository: IDashboardRepository,
    private val dataStore: IAppDataStore,
) {
    suspend operator fun invoke(status: ValveInteractionCommand): Result<Boolean> {
        return try {

            val meterAddress = dataStore.getPreference(DataStoreKeys.METER_ADDRESS_KEY, "").first()

            val valveCommand = ValveControlCommand.toCommand(
                request = ValveControlRequest(status = status),
                meterConfig = MeterConfig(meterAddress = meterAddress)
            )

            repository.connectAndWrite(
                service = ValveControlCommand.serviceUuid,
                writeCharacteristic = ValveControlCommand.characteristicUuid,
                value = valveCommand
            )

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}