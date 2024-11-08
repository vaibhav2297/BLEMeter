package com.example.blemeter.feature.dashboard.domain.usecases

import com.example.blemeter.config.extenstions.fromHexToUByteArray
import com.example.blemeter.config.model.MeterConfig
import com.example.blemeter.config.model.MeterType
import com.example.blemeter.core.ble.domain.command.Command
import com.example.blemeter.core.ble.domain.command.PurchaseDataCommand
import com.example.blemeter.core.ble.domain.model.request.PurchaseDataRequest
import com.example.blemeter.core.local.DataStore
import com.example.blemeter.feature.dashboard.domain.repository.IDashboardRepository
import com.example.local.datastore.DataStoreKeys
import com.example.local.datastore.IAppDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

abstract class BaseCommandDataUseCase(
    private val repository: IDashboardRepository,
    private val dataStore: IAppDataStore
) {

    @OptIn(ExperimentalUnsignedTypes::class)
    suspend operator fun <T, R, C : Command<T, R>> invoke(command: C): Result<Boolean> {
        return try {

            repository.connectAndWrite(
                service = command.serviceUuid,
                writeCharacteristic = command.characteristicUuid,
                value = setUpCommand()
            )

            Result.success(true)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    abstract fun setUpCommand() : UByteArray

    protected suspend fun getMeterConfig(): MeterConfig {
        val meterAddress = dataStore.getPreference(DataStoreKeys.METER_ADDRESS_KEY, "").first()
        val meterType =
            dataStore.getPreference(DataStoreKeys.METER_TYPE_KEY, MeterType.Unknown.code.toInt())
                .first().run { MeterType.getMeterType(this.toUInt()) }

        return MeterConfig(
            meterAddress = meterAddress,
            meterType = meterType
        )
    }
}