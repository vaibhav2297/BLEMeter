package com.example.blemeter.core.ble.utils

import com.example.blemeter.config.BLEMeterConfig
import java.nio.ByteOrder


fun String.fromHexToByteArray(): ByteArray {
    check(length % 2 == 0) { "Must have an even length" }

    return chunked(2).map { it.toInt(16).toByte() }.toByteArray()
}

fun List<Byte>.toHexString(): String = map { String.format("%02x", it) }.toList().toString()

fun ByteArray.toHexString(): String = map { String.format("%02x", it) }.toList().toString()

fun Char.intoByte(): Byte = this.code.toByte()
fun Char.intoUByte(): UByte = this.code.toUByte()

fun Byte.intoChar(): Char = this.toInt().toChar()

fun Byte.getBit(position: Int): Int {
    if (position !in 0..7) {
        throw IllegalArgumentException("Bit position must be between 0 and 7")
    }
    return (this.toInt() shr position) and 1
}

fun Byte.getBits(start: Int, end: Int): Int {
    if (start !in 0..7 || end !in 0..7 || start > end) {
        throw IllegalArgumentException("Invalid bit range. Start index must be between 0 and 7, and end index must be greater than or equal to start index.")
    }

    // Create a mask with 1s at the desired bit positions
    val mask = ((1 shl (end + 1)) - 1) shr (start + 1)

    // Perform bitwise AND with the mask to extract the desired bits
    return this.toInt() and mask
}


//Short Conversations
fun Short.toLowByte(): Byte = (this.toInt() and 0xFF).toByte()
fun Short.toHighByte(): Byte = ((this.toInt() shr 8) and 0xFF).toByte()

fun Short.toBytes(byteOrder: ByteOrder = BLEMeterConfig.defaultByteOrder): ByteArray {
    val bytes = ByteArray(2)
    val value = this.toInt()

    when (byteOrder) {
        ByteOrder.BIG_ENDIAN -> {
            bytes[0] = (value shr 8).toByte() //Highest Byte
            bytes[1] = (value and 0xFF).toByte() //Lowest Byte
        }
        ByteOrder.LITTLE_ENDIAN -> {
            bytes[0] = (value and 0xFF).toByte() //Lowest Byte
            bytes[1] = (value shr 8).toByte() // Highest Byte
        }
    }

    return bytes
}

/**
 * Int to Byte Conversions
 */

fun Int.toLowByte(): Byte = (this and 0xFF).toByte()
fun Int.toHighByte(): Byte = ((this shr 8) and 0xFF).toByte()
fun Int.toMidByte(): Byte = ((this shr 16) and 0xFF).toByte()

fun Int.to4ByteArray(): ByteArray {
    val byteArray = ByteArray(4)

    byteArray[0] = (this and 0xFF).toByte()             // Lowest byte
    byteArray[1] = ((this shr 8) and 0xFF).toByte()     // Second lowest byte
    byteArray[2] = ((this shr 16) and 0xFF).toByte()    // Second highest byte
    byteArray[3] = ((this shr 24) and 0xFF).toByte()    // Highest byte

    return byteArray
}

/**
 * Returns a 16-bit signed integer converted from 2 bytes at a specified position in a byte array.
 */
fun ByteArray.toInt16(startIndex: Int, byteOrder: ByteOrder = BLEMeterConfig.defaultByteOrder): Short {
    require(startIndex + 2 <= size) { "Not enough bytes in the array for a Short" }

    val byte1 = this[startIndex].toInt()
    val byte2 = this[startIndex + 1].toInt()

    val result = when (byteOrder) {
        ByteOrder.LITTLE_ENDIAN -> (byte2 shl 8) or (byte1 and 0xFF)
        ByteOrder.BIG_ENDIAN -> (byte1 shl 8) or (byte2 and 0xFF)
        else -> 0
    }

    return result.toShort()
}

/**
 * Returns a 24-bit unsigned integer converted from 3 bytes at a specified position in a byte array.
 */
fun ByteArray.toInt24(startIndex: Int, byteOrder: ByteOrder = BLEMeterConfig.defaultByteOrder): Int {
    require(startIndex + 3 <= size) { "Not enough bytes in the array for an Int24" }

    val bytes = this.slice(startIndex until startIndex + 3)

    return when (byteOrder) {
        ByteOrder.LITTLE_ENDIAN -> {
            (bytes[2].toInt() shl 16) or
                    ((bytes[1].toInt() and 0xFF) shl 8) or
                    (bytes[0].toInt() and 0xFF)
        }

        ByteOrder.BIG_ENDIAN -> {
            (bytes[0].toInt() shl 16) or
                    ((bytes[1].toInt() and 0xFF) shl 8) or
                    (bytes[0].toInt() and 0xFF)
        }

        else -> 0
    }
}

/**
 * Returns a 32-bit signed integer converted from 4 bytes at a specified position in a byte array.
 */
fun ByteArray.toInt32(startIndex: Int, byteOrder: ByteOrder = BLEMeterConfig.defaultByteOrder): Int {
    require(startIndex + 4 <= size) { "Not enough bytes in the array for an Int32" }

    val bytes = this.slice(startIndex until startIndex + 4)

    return when (byteOrder) {
        ByteOrder.LITTLE_ENDIAN -> {
            (bytes[3].toInt() shl 24) or
                    ((bytes[2].toInt() and 0xFF) shl 16) or
                    ((bytes[1].toInt() and 0xFF) shl 8) or
                    (bytes[0].toInt() and 0xFF)
        }

        ByteOrder.BIG_ENDIAN -> {
            (bytes[0].toInt() shl 24) or
                    ((bytes[1].toInt() and 0xFF) shl 16) or
                    ((bytes[2].toInt() and 0xFF) shl 8) or
                    (bytes[0].toInt() and 0xFF)
        }

        else -> 0
    }
}

fun ByteArray.accumulateSum(startIndex: Int, endIndex: Int): Byte {
    var sum = 0
    for (i in startIndex until endIndex) {
        sum += this[i].toInt() and 0xFF
    }
    return sum.toLowByte()
}

/*fun Byte.getBits(byte: Byte, startBit: Int, endBit: Int): Int {
    require(startBit in 0..7 && endBit in 0..7 && startBit <= endBit) {
        "Invalid bit range. startBit and endBit should be between 0 and 7, and startBit should be less than or equal to endBit."
    }

    val mask = (0xff shr (8 - endBit + startBit - 1)) and (0xff shl startBit)
    return (byte.toInt() and mask) shr startBit
}*/







