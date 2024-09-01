package com.example.blemeter.feature.dashboard.domain.usecases

import com.example.blemeter.config.model.MeterConfig
import com.example.blemeter.core.ble.data.BLEService
import com.example.blemeter.core.ble.domain.bleparsable.AccumulateCommand
import com.example.blemeter.core.ble.domain.bleparsable.PurchaseDataCommand
import com.example.blemeter.core.ble.domain.model.request.BaseRequest
import com.example.blemeter.core.ble.domain.model.request.PurchaseDataRequest
import com.example.blemeter.core.ble.utils.toHexString
import com.example.blemeter.core.local.DataStore
import com.example.blemeter.core.logger.ExceptionHandler
import com.example.blemeter.core.logger.ILogger
import com.example.blemeter.feature.dashboard.domain.repository.IDashboardRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PurchaseDataUseCase @Inject constructor(
    private val repository: IDashboardRepository,
    private val dataStore: DataStore
) {
    @OptIn(ExperimentalUnsignedTypes::class)
    suspend operator fun invoke(request: PurchaseDataRequest) : Result<Boolean> {
        return try {

            val meterAddress = dataStore.getMeterAddress().first()

            val purchaseCommand = PurchaseDataCommand.toCommand(
                request = request,
                meterConfig = MeterConfig(meterAddress = meterAddress)
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