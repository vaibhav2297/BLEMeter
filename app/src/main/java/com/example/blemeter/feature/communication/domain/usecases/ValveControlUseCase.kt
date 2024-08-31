package com.example.blemeter.feature.communication.domain.usecases

import com.example.blemeter.core.ble.data.BLEService
import com.example.blemeter.core.ble.data.local.MockDataProvider
import com.example.blemeter.core.ble.domain.bleparsable.ReadMeterDataCommand
import com.example.blemeter.core.ble.domain.bleparsable.ValveControlCommand
import com.example.blemeter.core.ble.domain.model.request.BaseRequest
import com.example.blemeter.core.ble.domain.model.request.MeterDataRequest
import com.example.blemeter.core.ble.domain.model.request.ValveControlCommandStatus
import com.example.blemeter.core.ble.domain.model.request.ValveControlRequest
import com.example.blemeter.core.ble.utils.fromHexToUByteArray
import com.example.blemeter.core.local.DataStore
import com.example.blemeter.core.logger.ExceptionHandler
import com.example.blemeter.core.logger.ILogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@OptIn(ExperimentalUnsignedTypes::class)
class ValveControlUseCase @Inject constructor(
    private val bleService: BLEService,
    private val exceptionHandler: ExceptionHandler,
    private val dataStore: DataStore,
    private val logger: ILogger
) {
    suspend operator fun invoke(status: ValveControlCommandStatus): Result<Boolean> {
        return try {

            logger.d("ValveControlUseCase :: statue :: $status")

            val valveCommand = ValveControlCommand.toCommand(ValveControlRequest(status = status))

            bleService.connectAndWrite {
                logger.d("ValveControlUseCase :: Connection Establish. valve control Command")
                bleService.writeCharacteristics(
                    service = ValveControlCommand.serviceUuid,
                    writeCharacteristic = ValveControlCommand.characteristicUuid,
                    value = valveCommand
                )
            }

            Result.success(true)
        } catch (e: Exception) {
            exceptionHandler.handle(e)
            Result.failure(e)
        }
    }
}