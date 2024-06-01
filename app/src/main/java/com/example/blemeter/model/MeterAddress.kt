package com.example.blemeter.model

import com.example.blemeter.core.ble.utils.to4ByteArray
import com.example.blemeter.core.ble.utils.toHighByte
import com.example.blemeter.core.ble.utils.toInt16
import com.example.blemeter.core.ble.utils.toInt32
import com.example.blemeter.core.ble.utils.toLowByte

data class MeterAddress(
    val addressCode: Int = -1,
    val meterType: MeterType = MeterType.Unknown,
    val manufacturerCode: Short = -1
) {

    @OptIn(ExperimentalUnsignedTypes::class)
    fun toByteArray(): ByteArray {
        // Create a ByteBuffer with capacity 7 bytes
        val byteArray = ByteArray(7)

        // Put addressCode as 4 bytes
        addressCode.to4ByteArray().copyInto(byteArray, 0)

        // Put meterType as 1 byte
        byteArray[4] = meterType.code.toByte()

        // Put manufacturerCode as 2 bytes
        byteArray[5] = manufacturerCode.toLowByte()
        byteArray[6] = manufacturerCode.toHighByte()

        // Return the ByteArray
        return byteArray
    }

    companion object {
        @OptIn(ExperimentalUnsignedTypes::class)
        @Throws
        fun ByteArray.toMeterAddress(): MeterAddress {
            require(this.size > 6) { "Not enough bytes to retrieve meter address" }

            val addressCode = this.toInt32(0)
            val meterType = MeterType.getMeterType(this[4])
            val manufacturerCode = this.toInt16(5)

            return MeterAddress(
                addressCode = addressCode.toInt(),
                meterType = meterType,
                manufacturerCode = manufacturerCode.toShort()
            )
        }

        @OptIn(ExperimentalUnsignedTypes::class)
        fun ByteArray.retrieveMeterType(): MeterType {
            require(this.size > 6) { "Not enough bytes to retrieve meter address" }

            return MeterType.getMeterType(this[4])
        }
    }
}

