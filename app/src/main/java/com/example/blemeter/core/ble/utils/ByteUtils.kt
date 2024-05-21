package com.example.blemeter.core.ble.utils

import java.nio.ByteOrder


fun String.fromHexToByteArray(): ByteArray {
    check(length % 2 == 0) { "Must have an even length" }

    return chunked(2).map { it.toInt(16).toByte() }.toByteArray()
}

@OptIn(ExperimentalUnsignedTypes::class)
fun String.fromHexToUByteArray(): UByteArray {
    check(length % 2 == 0) { "Must have an even length" }

    return chunked(2).map { it.toInt(16).toUByte() }.toUByteArray()
}

fun List<Byte>.toHexString(): String = map { String.format("%02x", it) }.toList().toString()

fun ByteArray.toHexString(): String = map { String.format("%02x", it) }.toList().toString()

@OptIn(ExperimentalUnsignedTypes::class)
fun UByteArray.toHexString(): String = this.toByteArray().toHexString()

fun Char.intoByte(): Byte = this.code.toByte()
fun Char.intoUByte(): UByte = this.code.toUByte()

fun Byte.intoChar(): Char = this.toInt().toChar()


/* Long -> byte conversions */
fun Long.toLowByte(): Byte = (this and 0xFF).toByte()
fun Long.toHighByte(): Byte = ((this shr 8) and 0xFF).toByte()
fun Long.toLowUByte(): UByte = (this and 0xFF).toUByte()
fun Long.toHighUByte(): UByte = ((this shr 8) and 0xFF).toUByte()

fun Long.to4ByteArray(): ByteArray {
    val byteArray = ByteArray(4)

    byteArray[0] = (this and 0xFF).toByte()             // Lowest byte
    byteArray[1] = ((this shr 8) and 0xFF).toByte()     // Second lowest byte
    byteArray[2] = ((this shr 16) and 0xFF).toByte()    // Second highest byte
    byteArray[3] = ((this shr 24) and 0xFF).toByte()    // Highest byte

    return byteArray
}

@OptIn(ExperimentalUnsignedTypes::class)
fun Long.to4UByteArray(): UByteArray {
    val byteArray = UByteArray(4)

    byteArray[0] = (this and 0xFF).toUByte()             // Lowest byte
    byteArray[1] = ((this shr 8) and 0xFF).toUByte()     // Second lowest byte
    byteArray[2] = ((this shr 16) and 0xFF).toUByte()    // Second highest byte
    byteArray[3] = ((this shr 24) and 0xFF).toUByte()    // Highest byte

    return byteArray
}


/* Int -> byte conversions */
fun Int.toLowByte(): Byte = (this and 0xFF).toByte()
fun Int.toHighByte(): Byte = ((this shr 8) and 0xFF).toByte()
fun Int.toMidByte(): Byte = ((this shr 16) and 0xFF).toByte()

fun Int.toLowUByte(): UByte = (this and 0xFF).toUByte()
fun Int.toHighUByte(): UByte = ((this shr 8) and 0xFF).toUByte()

fun Float.toLowUByte() = this.toInt().toLowUByte()

fun Float.toHighUByte() = this.toInt().toHighUByte()

/**
 * Returns a 16-bit signed integer converted from 2 bytes at a specified position in a byte array.
 */
fun List<Byte>.toInt16(startIndex: Int, byteOrder: ByteOrder = ByteOrder.nativeOrder()): Int {
    require(value = startIndex + 2 <= this.size) { "Not enough bytes in the array for a UInt16" }

    val bytes = this.subList(startIndex, startIndex + 2)

    return when (byteOrder) {
        ByteOrder.LITTLE_ENDIAN -> (bytes[1].toInt() shl 8) or bytes[0].toInt()
        ByteOrder.BIG_ENDIAN -> (bytes[0].toInt() shl 8) or bytes[1].toInt()
        else -> 0
    }
}

/**
 * Returns a 16-bit unsigned integer converted from 2 bytes at a specified position in a byte array.
 */
@OptIn(ExperimentalUnsignedTypes::class)
fun List<UByte>.toUInt16(startIndex: Int, byteOrder: ByteOrder = ByteOrder.nativeOrder()): UInt {
    require(value = startIndex + 2 <= this.size) { "Not enough bytes in the array for a UInt16" }

    val bytes = this.subList(startIndex, startIndex + 2)

    return when (byteOrder) {
        ByteOrder.LITTLE_ENDIAN -> (bytes[1].toUInt() shl 8) or bytes[0].toUInt()
        ByteOrder.BIG_ENDIAN -> (bytes[0].toUInt() shl 8) or bytes[1].toUInt()
        else -> 0u
    }

}

/**
 * Returns a 16-bit unsigned integer converted from 2 bytes at a specified position in a byte array.
 */
@OptIn(ExperimentalUnsignedTypes::class)
fun UByteArray.toUInt16(startIndex: Int, byteOrder: ByteOrder = ByteOrder.nativeOrder()): UInt {
    require(startIndex + 2 <= size) { "Not enough bytes in the array for a UInt16" }

    val byte1 = this[startIndex].toUInt()
    val byte2 = this[startIndex + 1].toUInt()

    return when (byteOrder) {
        ByteOrder.LITTLE_ENDIAN -> (byte2 shl 8) or byte1
        ByteOrder.BIG_ENDIAN -> (byte1 shl 8) or byte2
        else -> 0u
    }
}

/**
 * Returns a 24-bit signed integer converted from 3 bytes at a specified position in a byte array.
 */
fun List<Byte>.toInt24(startIndex: Int, byteOrder: ByteOrder = ByteOrder.nativeOrder()): Int {
    require(startIndex + 3 <= this.size) { "Not enough bytes in the array for a UInt24" }

    val bytes = this.subList(startIndex, startIndex + 3)

    return when (byteOrder) {
        ByteOrder.LITTLE_ENDIAN -> {
            (bytes[2].toInt() shl 16) or
                    (bytes[1].toInt() shl 8) or bytes[0].toInt()
        }

        ByteOrder.BIG_ENDIAN -> {
            (bytes[0].toInt() shl 16) or
                    (bytes[1].toInt() shl 8) or
                    bytes[2].toInt()
        }

        else -> 0
    }
}

/**
 * Returns a 32-bit signed integer converted from 4 bytes at a specified position in a byte array.
 */
fun List<Byte>.toInt32(startIndex: Int, byteOrder: ByteOrder = ByteOrder.nativeOrder()): Int {
    require(value = startIndex + 4 <= this.size) { "Not enough bytes in the array for a UInt16" }

    val bytes = this.subList(startIndex, startIndex + 4)

    return when (byteOrder) {
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
}

/**
 * Returns a 32-bit unsigned integer converted from 4 bytes at a specified position in a byte array.
 */
fun List<UByte>.toUInt32(startIndex: Int, byteOrder: ByteOrder = ByteOrder.nativeOrder()): UInt {
    require(value = startIndex + 4 <= this.size) { "Not enough bytes in the array for a UInt16" }

    val bytes = this.subList(startIndex, startIndex + 4)

    return when (byteOrder) {
        ByteOrder.LITTLE_ENDIAN -> {
            (bytes[3].toUInt() shl 24) or
                    (bytes[2].toUInt() shl 16) or
                    (bytes[1].toUInt() shl 8) or
                    bytes[0].toUInt()
        }

        ByteOrder.BIG_ENDIAN -> {
            (bytes[0].toUInt() shl 24) or
                    (bytes[1].toUInt() shl 16) or
                    (bytes[2].toUInt() shl 8) or
                    bytes[3].toUInt()
        }

        else -> 0u
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
fun UByteArray.toUInt24(startIndex: Int, byteOrder: ByteOrder = ByteOrder.nativeOrder()): UInt {
    require(startIndex + 3 <= size) { "Not enough bytes in the array for a UInt24" }

    val bytes = this.slice(startIndex until startIndex + 3)

    return when (byteOrder) {
        ByteOrder.LITTLE_ENDIAN -> {
            (bytes[2].toUInt() shl 16) or
                    (bytes[1].toUInt() shl 8) or
                    bytes[0].toUInt()
        }

        ByteOrder.BIG_ENDIAN -> {
            (bytes[0].toUInt() shl 16) or
                    (bytes[1].toUInt() shl 8) or
                    bytes[2].toUInt()
        }

        else -> 0u
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
fun UByteArray.toUInt32(startIndex: Int, byteOrder: ByteOrder = ByteOrder.nativeOrder()): UInt {
    require(startIndex + 4 <= size) { "Not enough bytes in the array for a UInt32" }

    val bytes = this.slice(startIndex until startIndex + 4)

    return when (byteOrder) {
        ByteOrder.LITTLE_ENDIAN -> {
            (bytes[3].toUInt() shl 24) or
                    (bytes[2].toUInt() shl 16) or
                    (bytes[1].toUInt() shl 8) or
                    bytes[0].toUInt()
        }

        ByteOrder.BIG_ENDIAN -> {
            (bytes[0].toUInt() shl 24) or
                    (bytes[1].toUInt() shl 16) or
                    (bytes[2].toUInt() shl 8) or
                    bytes[3].toUInt()
        }

        else -> 0u
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
fun UByteArray.buildBleFrame(): UByteArray =
    ubyteArrayOf(
        BLEConstants.SOF.toUByte(),
        *this,
        BLEConstants.EOF.toUByte()
    )

fun Byte.getBits(byte: Byte, startBit: Int, endBit: Int): Int {
    require(startBit in 0..7 && endBit in 0..7 && startBit <= endBit) {
        "Invalid bit range. startBit and endBit should be between 0 and 7, and startBit should be less than or equal to endBit."
    }

    val mask = (0xff shr (8 - endBit + startBit - 1)) and (0xff shl startBit)
    return (byte.toInt() and mask) shr startBit
}

fun UByte.getBits(startBit: Int, endBit: Int): Int {
    require(startBit in 0..7 && endBit in 0..7 && startBit <= endBit) {
        "Invalid bit range. startBit and endBit should be between 0 and 7, and startBit should be less than or equal to endBit."
    }

    val mask = (0xff shr (8 - endBit + startBit - 1)) and (0xff shl startBit)
    return (this.toInt() and mask) shr startBit
}







