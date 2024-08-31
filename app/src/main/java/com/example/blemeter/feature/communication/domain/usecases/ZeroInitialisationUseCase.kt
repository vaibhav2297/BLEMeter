package com.example.blemeter.feature.communication.domain.usecases

import com.example.blemeter.core.ble.data.BLEService
import com.example.blemeter.core.ble.data.local.MockDataProvider
import com.example.blemeter.core.ble.domain.bleparsable.ReadMeterDataCommand
import com.example.blemeter.core.ble.domain.bleparsable.ZeroInitialiseCommand
import com.example.blemeter.core.ble.domain.model.request.MeterDataRequest
import com.example.blemeter.core.ble.utils.fromHexToUByteArray
import com.example.blemeter.core.local.DataStore
import com.example.blemeter.core.logger.ExceptionHandler
import com.example.blemeter.core.logger.ILogger
import kotlinx.coroutines.delay
import javax.inject.Inject

class ZeroInitialisationUseCase @Inject constructor(
    private val bleService: BLEService,
    private val exceptionHandler: ExceptionHandler,
    private val dataStore: DataStore,
    private val logger: ILogger
) {
    @OptIn(ExperimentalUnsignedTypes::class)
    suspend operator fun invoke() : Result<Boolean> {
        return try {

            val zeroInitialiseCommand = ZeroInitialiseCommand.toCommand(MeterDataRequest())

            bleService.connectAndWrite {
                logger.d("ZeroInitialisationUseCase :: Connection Establish. Zero Initialising")
                bleService.writeCharacteristics(
                    service = ReadMeterDataCommand.serviceUuid,
                    writeCharacteristic = ReadMeterDataCommand.characteristicUuid,
                    value = zeroInitialiseCommand
                )
            }

            Result.success(true)

        } catch (e: Exception) {
            exceptionHandler.handle(e)
            Result.failure(e)
        }
    }
}