package com.example.blemeter.config.extenstions

import kotlin.jvm.Throws
import kotlin.math.pow
import kotlin.math.roundToInt


fun String.chunkAndReverseString(): String {
    check(length % 2 == 0) { "Must have an even length" }

    return this
        .chunked(2)
        .reversed()
        .joinToString("")
}

@Throws
fun String.getMeterAddress(): String {
    check(this.length >= 14 ) { "Not enough size to extract meter address" }

    return this.takeLast(14)
}

@Throws
fun String.getMeterType(): String {
    check(this.length == 14 ) { "Size of meter address is not proper" }

    return this.substring(8..9)
}

fun String.isDecimal() =
    this.matches("\\d+(?:\\.\\d+)?".toRegex())

fun Double.roundTo(numFractionDigits: Int): Double {
    val factor = 10.0.pow(numFractionDigits.toDouble())
    return (this * factor).roundToInt() / factor
}

fun Float.roundTo(numFractionDigits: Int): Float {
    val factor = 10.0f.pow(numFractionDigits.toFloat())
    return (this * factor).roundToInt() / factor
}
