package com.example.blemeter.core.ble.domain.model

import com.example.blemeter.core.ble.utils.toUInt16

enum class DataIdentifier(val identifier: Int ) {
    NONE(-1),
    METER_DATA(36895),  //901F hex
    VALVE_CONTROL_DATA(40983); //A017 hex

    companion object {
        @OptIn(ExperimentalUnsignedTypes::class)
        fun getDataType(command: UByteArray) : DataIdentifier {
            val identifier = command.toUInt16(11)
            return when(identifier) {
                METER_DATA.identifier.toUInt() -> METER_DATA
                VALVE_CONTROL_DATA.identifier.toUInt() -> VALVE_CONTROL_DATA
                else -> NONE
            }
        }
    }
}