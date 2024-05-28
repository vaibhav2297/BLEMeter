package com.example.blemeter.core.ble.domain.usecases

import android.bluetooth.BluetoothGattCharacteristic
import com.example.blemeter.core.ble.domain.bleparsable.MeterDataCommand
import com.example.blemeter.core.ble.domain.bleparsable.ValveControlCommand
import com.example.blemeter.core.ble.domain.model.DataIdentifier
import com.example.blemeter.core.logger.ExceptionHandler
import com.example.blemeter.core.logger.ILogger
import com.example.blemeter.model.Data
import javax.inject.Inject

@Suppress("DEPRECATION")
@OptIn(ExperimentalUnsignedTypes::class)
class ParseWrite @Inject constructor(
    private val logger: ILogger,
    private val exceptionHandler: ExceptionHandler
) {

    operator fun invoke(
        characteristic: BluetoothGattCharacteristic
    ): Result<Data?> {
        return characteristic.value?.let { value ->
            parseCommandAndCreateData(value.toUByteArray())
        } ?: Result.success(null)
    }

    private fun parseCommandAndCreateData(value: UByteArray) : Result<Data?> {
        val dataIdentifier = DataIdentifier.getDataType(value)
        logger.d("Data Type : ${dataIdentifier.name}")
        return try {
            val data = when(dataIdentifier) {
                DataIdentifier.METER_DATA -> MeterDataCommand.fromCommand(value)
                DataIdentifier.VALVE_CONTROL_DATA -> ValveControlCommand.fromCommand(value)
                else -> null
            }
            Result.success(data)
        } catch (e: Exception) {
            exceptionHandler.handle(e)
            Result.failure(e)
        }
    }
}