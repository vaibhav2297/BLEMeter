package com.example.blemeter.model

import com.example.blemeter.core.ble.utils.getBit

enum class BatteryVoltage(val code: Int) {
    UNKNOWN(-1),
    NORMAL(0),
    UNDER_VOLTAGE(1);


    companion object {
        fun getBatteryVoltage(byteValue: Byte): BatteryVoltage {
            //val batteryBit = (byteValue.toInt() shr 2) and 1
            return when (val batteryBit = byteValue.getBit(2)) {
                0 -> NORMAL
                1 -> UNDER_VOLTAGE
                else -> throw IllegalArgumentException("Invalid binary string for battery voltage: $batteryBit")
            }
        }
    }
}