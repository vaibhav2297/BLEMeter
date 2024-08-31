package com.example.blemeter.core.ble.utils

object BLEConstants {

    const val MTU = 20

    const val SOF: UByte = 0x68u //Start of Frame

    const val EOF: UByte = 0x16u //End of Frame

    // Meter number : 71 96 24 03 18 00 02
    const val METER_ADDRESS = "01001803249671"

    const val METER_TYPE: UByte = 0x12u

    const val SERIAL_NUMBER: UByte = 0xD0u

    const val STAND_BY: UByte = 0x00u
}