package com.example.blemeter.core.ble.utils

object BLEConstants {

    const val MTU = 20

    const val SOF: UByte = 104u //Start of Frame

    const val EOF: UByte = 22u //End of Frame

    const val METER_ADDRESS = "AA5504B10000B5"

    const val COMMAND_RESPONSE_SIZE = 40

    const val STAND_BY: UByte = 0u

    const val IS_CONFIGURATION_MODE = false
}