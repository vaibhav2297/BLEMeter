package com.example.blemeter.core.ble.domain.usecases

import android.bluetooth.BluetoothGattCharacteristic
import com.example.blemeter.core.ble.domain.bleparsable.MeterDataCommand
import com.example.blemeter.core.ble.domain.bleparsable.ValveControlCommand
import com.example.blemeter.core.ble.domain.model.DataType
import com.example.blemeter.model.Data

@Suppress("DEPRECATION")
@OptIn(ExperimentalUnsignedTypes::class)
class ParseWrite {

    operator fun invoke(
        characteristic: BluetoothGattCharacteristic
    ): Data? {
        return characteristic.value?.let { value ->
            parseCommandAndCreateData(value.toUByteArray())
        }
    }

    private fun parseCommandAndCreateData(value: UByteArray) : Data? {
        val dataType = DataType.getDataType(value)
        return when(dataType) {
            DataType.METER_DATA -> MeterDataCommand.fromCommand(value)
            DataType.VALVE_CONTROL_DATA -> ValveControlCommand.fromCommand(value)
            else -> null
        }
    }
}