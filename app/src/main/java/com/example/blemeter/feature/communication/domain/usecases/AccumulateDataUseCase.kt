package com.example.blemeter.feature.communication.domain.usecases

import com.example.blemeter.core.ble.data.BLEService
import com.example.blemeter.core.ble.data.local.MockDataProvider
import com.example.blemeter.core.ble.domain.bleparsable.AccumulateCommand
import com.example.blemeter.core.ble.domain.bleparsable.ReadMeterDataCommand
import com.example.blemeter.core.ble.domain.model.request.AccumulateDataRequest
import com.example.blemeter.core.ble.domain.model.request.MeterDataRequest
import com.example.blemeter.core.ble.utils.fromHexToUByteArray
import com.example.blemeter.core.local.DataStore
import com.example.blemeter.core.logger.ExceptionHandler
import com.example.blemeter.core.logger.ILogger
import kotlinx.coroutines.delay
import javax.inject.Inject

class AccumulateDataUseCase @Inject constructor(
    private val bleService: BLEService,
    private val exceptionHandler: ExceptionHandler,
    private val dataStore: DataStore,
    private val logger: ILogger
) {
    @OptIn(ExperimentalUnsignedTypes::class)
    suspend operator fun invoke(request: AccumulateDataRequest) : Result<Boolean> {
        return try {

            val accumulationCommand = AccumulateCommand.toCommand(request)

            bleService.connectAndWrite {
                logger.d("AccumulateDataUseCase :: Connection Establish. Reading Accumulation data.")
                bleService.writeCharacteristics(
                    service = AccumulateCommand.serviceUuid,
                    writeCharacteristic = AccumulateCommand.characteristicUuid,
                    value = MockDataProvider.Accumulate.fromHexToUByteArray()
                )
            }

            Result.success(true)

        } catch (e: Exception) {
            exceptionHandler.handle(e)
            Result.failure(e)
        }
    }
}