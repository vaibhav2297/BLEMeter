package com.example.blemeter.config.model

import com.example.blemeter.config.extenstions.getBit

enum class BatteryVoltage(val code: UInt) {
    UNKNOWN(0xFFFFFFFFu),
    NORMAL(0x0u),
    UNDER_VOLTAGE(0x1u);


    companion object {
        fun getBatteryVoltage(byteValue: UByte): BatteryVoltage {
            //val batteryBit = (byteValue.toInt() shr 2) and 1
            return when (val batteryBit = byteValue.getBit(5)) {
                0u -> NORMAL
                1u -> UNDER_VOLTAGE
                else -> throw IllegalArgumentException("Invalid binary string for battery voltage: $batteryBit")
            }
        }
    }
}