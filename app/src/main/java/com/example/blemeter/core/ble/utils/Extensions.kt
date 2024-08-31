package com.example.blemeter.core.ble.utils

import kotlin.math.pow
import kotlin.math.roundToInt

fun Double.roundTo(numFractionDigits: Int): Double {
    val factor = 10.0.pow(numFractionDigits.toDouble())
    return (this * factor).roundToInt() / factor
}

fun Float.roundTo(numFractionDigits: Int): Float {
    val factor = 10.0f.pow(numFractionDigits.toFloat())
    return (this * factor).roundToInt() / factor
}


