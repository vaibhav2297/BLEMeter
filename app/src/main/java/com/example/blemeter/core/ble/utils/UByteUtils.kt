package com.example.blemeter.core.ble.utils

import com.example.blemeter.config.BLEMeterConfig
import java.nio.ByteOrder

@OptIn(ExperimentalUnsignedTypes::class)

fun String.fromHexToUByteArray(): UByteArray {
    check(length % 2 == 0) { "Must have an even length" }

    return chunked(2).map { it.toInt(16).toUByte() }.toUByteArray()
}

@OptIn(ExperimentalUnsignedTypes::class)
fun UByteArray.toHexString(): String = map { "%02x".format(it.toInt()) }.joinToString("")


/* Byte conversions*/
fun UByte.getBit(position: Int): UInt {
    if (position !in 0..7) {
        throw IllegalArgumentException("Bit position must be between 0 and 7")
    }
    return (this.toUInt() shr position) and 1u
}

fun UByte.getBits(start: Int, end: Int): UInt {
    if (start !in 0..7 || end !in 0..7 || start > end) {
        throw IllegalArgumentException("Invalid bit range. Start index must be between 0 and 7, and end index must be greater than or equal to start index.")
    }

    // Create a mask with 1s at the desired bit positions
    val mask = ((1 shl (end + 1)) - 1) xor ((1 shl start) - 1)

    // Perform bitwise AND with the mask to extract the desired bits
    return (this.toUInt() and mask.toUInt()) shr start
}


/* Short conversions*/
fun UShort.toLowByte(): UByte = (this.toInt() and 0xFF).toUByte()

fun UShort.toHighByte(): UByte = ((this.toInt() shr 8) and 0xFF).toUByte()

@OptIn(ExperimentalUnsignedTypes::class)
fun UShort.toBytes(byteOrder: ByteOrder = BLEMeterConfig.defaultByteOrder): UByteArray {
    val bytes = UByteArray(2)
    val value = this.toInt()

    when (byteOrder) {
        ByteOrder.BIG_ENDIAN -> {
            bytes[0] = (value shr 8).toUByte() // Highest Byte
            bytes[1] = (value and 0xFF).toUByte() // Lowest Byte
        }
        ByteOrder.LITTLE_ENDIAN -> {
            bytes[0] = (value and 0xFF).toUByte() // Lowest Byte
            bytes[1] = (value shr 8).toUByte() // Highest Byte
        }
    }

    return bytes
}

/* UInt conversions*/
fun UInt.toLowByte(): UByte = (this.and(0xFFu)).toUByte()
fun UInt.toHighByte(): UByte = ((this.shr(8)).and(0xFFu)).toUByte()
fun Int.toLowUByte(): UByte = (this and 0xFF).toUByte()
fun Int.toHighUByte(): UByte = ((this shr 8) and 0xFF).toUByte()

@OptIn(ExperimentalUnsignedTypes::class)
fun UInt.to4UByteArray(byteOrder: ByteOrder = BLEMeterConfig.defaultByteOrder): UByteArray {
    val byteArray = UByteArray(4)

    when(byteOrder) {
        ByteOrder.LITTLE_ENDIAN -> {
            byteArray[0] = (this.and(0xFFu)).toUByte()             // Lowest byte
            byteArray[1] = ((this.shr(8)).and(0xFFu)).toUByte()    // Second lowest byte
            byteArray[2] = ((this.shr(16)).and(0xFFu)).toUByte()   // Second highest byte
            byteArray[3] = ((this.shr(24)).and(0xFFu)).toUByte()   // Highest byte
        }
        ByteOrder.BIG_ENDIAN -> {
            byteArray[3] = (this.and(0xFFu)).toUByte()             // Lowest byte
            byteArray[2] = ((this.shr(8)).and(0xFFu)).toUByte()    // Second lowest byte
            byteArray[1] = ((this.shr(16)).and(0xFFu)).toUByte()   // Second highest byte
            byteArray[0] = ((this.shr(24)).and(0xFFu)).toUByte()   // Highest byte
        }
    }
    return byteArray
}

/**
 * Returns a 16-bit unsigned integer converted from 2 bytes at a specified position in a byte array.
 */
@OptIn(ExperimentalUnsignedTypes::class)
fun UByteArray.toUInt16(startIndex: Int, byteOrder: ByteOrder = BLEMeterConfig.defaultByteOrder): UShort {
    require(startIndex + 2 <= size) { "Not enough bytes in the array for a UShort" }

    val byte1 = this[startIndex].toUInt()
    val byte2 = this[startIndex + 1].toUInt()

    val result = when (byteOrder) {
        ByteOrder.LITTLE_ENDIAN -> (byte2 shl 8) or byte1
        ByteOrder.BIG_ENDIAN -> (byte1 shl 8) or byte2
        else -> 0u
    }

    return result.toUShort()
}

/**
 * Returns a 24-bit unsigned integer converted from 3 bytes at a specified position in a byte array.
 */
@OptIn(ExperimentalUnsignedTypes::class)
fun UByteArray.toUInt24(startIndex: Int, byteOrder: ByteOrder = BLEMeterConfig.defaultByteOrder): UInt {
    require(startIndex + 3 <= size) { "Not enough bytes in the array for a UInt24" }

    val bytes = this.slice(startIndex until startIndex + 3).map { it.toUInt() }

    return when (byteOrder) {
        ByteOrder.LITTLE_ENDIAN -> {
            (bytes[2] shl 16) or
                    (bytes[1] shl 8) or
                    bytes[0]
        }

        ByteOrder.BIG_ENDIAN -> {
            (bytes[0] shl 16) or
                    (bytes[1] shl 8) or
                    bytes[2]
        }

        else -> 0u
    }
}

/**
 * Returns a 32-bit unsigned integer converted from 4 bytes at a specified position in a byte array.
 */
@OptIn(ExperimentalUnsignedTypes::class)
fun UByteArray.toUInt32(startIndex: Int, byteOrder: ByteOrder = BLEMeterConfig.defaultByteOrder): UInt {
    require(startIndex + 4 <= size) { "Not enough bytes in the array for a UInt32" }

    val bytes = this.slice(startIndex until startIndex + 4).map { it.toUInt() }

    return when (byteOrder) {
        ByteOrder.LITTLE_ENDIAN -> {
            (bytes[3] shl 24) or
                    (bytes[2] shl 16) or
                    (bytes[1] shl 8) or
                    bytes[0]
        }

        ByteOrder.BIG_ENDIAN -> {
            (bytes[0] shl 24) or
                    (bytes[1] shl 16) or
                    (bytes[2] shl 8) or
                    bytes[3]
        }

        else -> 0u
    }
}

/**
 * Returns a 32-bit floating-point number converted from 4 bytes at a specified position in a byte array.
 */
@OptIn(ExperimentalUnsignedTypes::class)
fun UByteArray.toFloat32(startIndex: Int, byteOrder: ByteOrder = BLEMeterConfig.defaultByteOrder): Float {
    require(startIndex + 4 <= size) { "Not enough bytes in the array for a Float32" }

    val bytes = this.slice(startIndex until startIndex + 4).map { it.toUByte() }

    val bits = when (byteOrder) {
        ByteOrder.LITTLE_ENDIAN -> {
            (bytes[3].toInt() shl 24) or
                    (bytes[2].toInt() shl 16) or
                    (bytes[1].toInt() shl 8) or
                    bytes[0].toInt()
        }

        ByteOrder.BIG_ENDIAN -> {
            (bytes[0].toInt() shl 24) or
                    (bytes[1].toInt() shl 16) or
                    (bytes[2].toInt() shl 8) or
                    bytes[3].toInt()
        }

        else -> 0
    }

    return Float.fromBits(bits)
}

@OptIn(ExperimentalUnsignedTypes::class)
fun UByteArray.accumulateUByteArray(): UByte {
    var sum = 0

    for (value in this) {
        sum += value.toInt()
    }

    // Ensure the result does not exceed 0xFF (255)
    val result = sum and 0xFF

    return result.toUByte()
}

fun UByteArray.calculateBCDValue(): Int {
    var value = 0
    this.forEach { byte ->
        value = value * 100 + ((byte.toInt() shr 4) * 10 + (byte.toInt() and 0x0F))
    }
    return value
}

fun calculateBCDFloat(bytes: UByteArray): Float {
    var value = 0.0f
    var divisor = 1.0f
    bytes.reversed().forEach { byte ->
        val highNibble = (byte.toInt() shr 4).toFloat()
        val lowNibble = (byte.toInt() and 0x0F).toFloat()
        value += (highNibble * 10 + lowNibble) * divisor
        divisor *= 10000
    }
    return value
}