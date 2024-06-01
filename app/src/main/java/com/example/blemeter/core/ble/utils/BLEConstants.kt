package com.example.blemeter.core.ble.utils

object BLEConstants {

    const val MTU = 20

    const val SOF: Byte = 104 //Start of Frame

    const val EOF: Byte = 22 //End of Frame

    const val METER_ADDRESS = "AA5504B10000B5"

    const val COMMAND_RESPONSE_SIZE = 40

    const val STAND_BY: Byte = 0

    const val IS_CONFIGURATION_MODE = false
}