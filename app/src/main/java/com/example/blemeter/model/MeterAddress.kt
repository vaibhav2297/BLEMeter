package com.example.blemeter.model

import com.example.blemeter.core.ble.utils.toUInt16
import com.example.blemeter.core.ble.utils.toUInt32

data class MeterAddress(
    val addressCode: Int = -1,
    val meterType: MeterType = MeterType.Unknown,
    val manufacturerCode: Int = -1
) {

    companion object {
        @OptIn(ExperimentalUnsignedTypes::class)
        @Throws
        fun UByteArray.toMeterAddress(): MeterAddress {
            require(this.size > 6) { "Not enough bytes to retrieve meter address" }

            val addressCode = this.toUInt32(0)
            val meterType = MeterType.getMeterType(this[4])
            val manufacturerCode = this.toUInt16(5)

            return MeterAddress(
                addressCode = addressCode.toInt(),
                meterType = meterType,
                manufacturerCode = manufacturerCode.toInt()
            )
        }
    }
}

