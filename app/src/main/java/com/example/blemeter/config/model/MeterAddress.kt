package com.example.blemeter.config.model

import com.example.blemeter.config.extenstions.to4UByteArray
import com.example.blemeter.config.extenstions.toHighByte
import com.example.blemeter.config.extenstions.toLowByte
import com.example.blemeter.config.extenstions.toUInt16
import com.example.blemeter.config.extenstions.toUInt32

data class MeterAddress(
    val addressCode: UInt = 0u,
    val meterType: MeterType = MeterType.Unknown,
    val manufacturerCode: UShort = 0u
) {

    @OptIn(ExperimentalUnsignedTypes::class)
    fun toUByteArray(): UByteArray {
        // Create a ByteBuffer with capacity 7 bytes
        val byteArray = UByteArray(7)

        // Put addressCode as 4 bytes
        addressCode.to4UByteArray().copyInto(byteArray, 0)

        // Put meterType as 1 byte
        byteArray[4] = meterType.code.toUByte()

        // Put manufacturerCode as 2 bytes
        byteArray[5] = manufacturerCode.toLowByte()
        byteArray[6] = manufacturerCode.toHighByte()

        // Return the ByteArray
        return byteArray
    }

    companion object {
        @OptIn(ExperimentalUnsignedTypes::class)
        @Throws
        fun UByteArray.toMeterAddress(): MeterAddress {
            require(this.size > 6) { "Not enough bytes to retrieve meter address" }

            val addressCode = this.toUInt32(0)
            val meterType = MeterType.getMeterType(this[4])
            val manufacturerCode = this.toUInt16(5)

            return MeterAddress(
                addressCode = addressCode,
                meterType = meterType,
                manufacturerCode = manufacturerCode
            )
        }

        @OptIn(ExperimentalUnsignedTypes::class)
        fun UByteArray.retrieveMeterType(): MeterType {
            require(this.size > 6) { "Not enough bytes to retrieve meter address" }

            return MeterType.getMeterType(this[4])
        }
    }
}

