package com.example.blemeter.config.model

import com.example.blemeter.config.extenstions.getBit

/**
 * Enum class representing different In Place method for a meter.
 *
 * - [commandBit]: The bit used to send a command to the meter.
 * - [responseBit]: The bit expected in the response from the meter.
 *
 */
enum class InPlaceMethod(
    val commandBit: UInt,
    val responseBit:  UInt
) {
    NONE(
        commandBit = 255u,
        responseBit = 255u
    ),

    TWO_WIRE_ACTUATOR(
        commandBit = 0x44u,
        responseBit = 0b0u
    ),

    FIVE_WIRE_ACTUATOR(
        commandBit = 0x4bu,
        responseBit = 0b1u
    );


    companion object {
        fun getInPlaceMethodByCommandBit(commandBit: UInt): InPlaceMethod {
            return when (commandBit) {
                TWO_WIRE_ACTUATOR.commandBit -> TWO_WIRE_ACTUATOR
                FIVE_WIRE_ACTUATOR.commandBit -> FIVE_WIRE_ACTUATOR
                else -> throw IllegalArgumentException("Invalid value for the in place method: $commandBit")
            }
        }

        fun getInPlaceMethodByResponseBit(responseBit: UByte): InPlaceMethod {
            return when (responseBit.getBit(2)) {
                TWO_WIRE_ACTUATOR.responseBit -> TWO_WIRE_ACTUATOR
                FIVE_WIRE_ACTUATOR.responseBit -> FIVE_WIRE_ACTUATOR
                else -> throw IllegalArgumentException("Invalid value for the in place method: $responseBit")
            }
        }
    }
}