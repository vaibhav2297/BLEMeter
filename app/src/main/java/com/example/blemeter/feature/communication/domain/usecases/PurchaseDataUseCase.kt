package com.example.blemeter.feature.communication.domain.usecases

import com.example.blemeter.core.ble.data.BLEService
import com.example.blemeter.core.ble.domain.bleparsable.PurchaseDataCommand
import com.example.blemeter.core.ble.domain.model.request.BaseRequest
import com.example.blemeter.core.ble.domain.model.request.PurchaseDataRequest
import com.example.blemeter.core.ble.utils.toHexString
import com.example.blemeter.core.local.DataStore
import com.example.blemeter.core.logger.ExceptionHandler
import com.example.blemeter.core.logger.ILogger
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PurchaseDataUseCase @Inject constructor(
    private val bleService: BLEService,
    private val exceptionHandler: ExceptionHandler,
    private val dataStore: DataStore,
    private val logger: ILogger
) {
    @OptIn(ExperimentalUnsignedTypes::class)
    suspend operator fun invoke(request: PurchaseDataRequest) : Result<Boolean> {
        return try {

            val meterAddress = dataStore.getMeterAddress().first()

            val baseRequest = BaseRequest(
                meterAddress = meterAddress
            )

            val newRequest = request.copy(baseRequest = baseRequest)

            logger.d("PurchaseDataUseCase :: request :: $newRequest")

            val command = PurchaseDataCommand.toCommand(newRequest)

            logger.d("PurchaseDataUseCase :: command :: ${command.contentToString()}")
            logger.d("PurchaseDataUseCase :: command :: ${command.toHexString()}")

            bleService.connectAndWrite {
                logger.d("PurchaseDataUseCase :: Connection Establish. Writing Purchase Data.")
                bleService.writeCharacteristics(
                    service = PurchaseDataCommand.serviceUuid,
                    writeCharacteristic = PurchaseDataCommand.characteristicUuid,
                    value = command
                )
            }

            Result.success(true)

        } catch (e: Exception) {
            exceptionHandler.handle(e)
            Result.failure(e)
        }
    }
}