package com.example.blemeter.config

import java.nio.ByteOrder

data class MeterConfig(
    val byteOrder: ByteOrder
)

object BLEMeterConfig {

    val defaultByteOrder: ByteOrder = ByteOrder.BIG_ENDIAN

}