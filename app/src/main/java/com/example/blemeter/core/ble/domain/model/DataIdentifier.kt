package com.example.blemeter.core.ble.domain.model

import com.example.blemeter.core.ble.utils.toInt16

enum class DataIdentifier(val identifier: Int) {
    NONE(-1),
    METER_DATA(36895),  //901F hex
    VALVE_CONTROL_DATA(40983); //A017 hex

    companion object {
        fun getDataType(command: ByteArray) : DataIdentifier {
            val identifier = command.toInt16(11)
            return when(identifier) {
                METER_DATA.identifier.toShort() -> METER_DATA
                VALVE_CONTROL_DATA.identifier.toShort() -> VALVE_CONTROL_DATA
                else -> NONE
            }
        }
    }
}