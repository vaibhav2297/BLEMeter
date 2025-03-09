package com.example.blemeter.feature.dashboard.domain.usecases

import com.example.blemeter.config.extenstions.fromHexToUByteArray
import com.example.blemeter.config.model.MeterConfig
import com.example.blemeter.config.model.MeterType
import com.example.blemeter.core.ble.domain.command.PurchaseDataCommand
import com.example.blemeter.core.ble.domain.model.request.PurchaseDataRequest
import com.example.blemeter.core.local.DataStore
import com.example.blemeter.feature.dashboard.domain.repository.IDashboardRepository
import com.example.local.datastore.DataStoreKeys
import com.example.local.datastore.IAppDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PurchaseDataUseCase @Inject constructor(
    private val repository: IDashboardRepository,
    private val dataStore: IAppDataStore
) {
    @OptIn(ExperimentalUnsignedTypes::class)
    suspend operator fun invoke(request: PurchaseDataRequest) : Result<Boolean> {
        return try {

            val meterAddress = dataStore.getPreference(DataStoreKeys.METER_ADDRESS_KEY, "").first()
            val meterType = dataStore.getPreference(DataStoreKeys.METER_CALIBRATION_TYPE, 0)
                .first()
                .run {
                    if (this == 0) MeterType.WaterMeter.DrinkingWaterMeter
                    else MeterType.getMeterType(this.toUInt())
                }

            val purchaseCommand = PurchaseDataCommand.toCommand(
                request = request,
                meterConfig = MeterConfig(
                    meterAddress = meterAddress,
                    meterType = meterType
                )
            )

            repository.connectAndWrite(
                service = PurchaseDataCommand.serviceUuid,
                writeCharacteristic = PurchaseDataCommand.characteristicUuid,
                value = purchaseCommand
            )

            Result.success(true)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}