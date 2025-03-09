package com.example.blemeter.config.model

import com.example.blemeter.config.extenstions.getBits

/**
 * Enum class representing different calibration levels for a meter.
 *
 * - [commandBit]: The bit used to send a command to the meter.
 * - [responseBit]: The bit expected in the response from the meter.
 *
 */
enum class CalibrationIdentification(
    val commandBit: UInt,
    val responseBit: UInt,
    val factor: UInt
) {
    NONE(
        commandBit = 255u,
        responseBit = 0xFFFFFFFFu,
        factor = 1u
    ),

    ONE_LITRE(
        commandBit = 0x18u,
        responseBit = 0b00u,
        factor = 1u
    ),

    TEN_LITRE(
        commandBit = 0x12u,
        responseBit = 0b01u,
        factor = 10u
    ),

    HUNDRED_LITRE(
        commandBit = 0x10u,
        responseBit = 0b10u,
        factor = 100u
    ),

    THOUSAND_LITRE(
        commandBit = 0x15u,
        responseBit = 0b11u,
        factor = 1000u
    );

    companion object {
        fun getCalibrationIdentificationByCommandBit(commandBit: UInt): CalibrationIdentification {
            return when (commandBit) {
                ONE_LITRE.commandBit -> ONE_LITRE
                TEN_LITRE.commandBit -> TEN_LITRE
                HUNDRED_LITRE.commandBit -> HUNDRED_LITRE
                THOUSAND_LITRE.commandBit -> THOUSAND_LITRE
                else -> throw IllegalArgumentException("Invalid value for the calibration identification: $commandBit")
            }
        }

        fun getCalibrationIdentificationByResponseBit(responseBit: UByte): CalibrationIdentification {
            return when (responseBit.getBits(0,1)) {
                ONE_LITRE.responseBit -> ONE_LITRE
                TEN_LITRE.responseBit -> TEN_LITRE
                HUNDRED_LITRE.responseBit -> HUNDRED_LITRE
                THOUSAND_LITRE.responseBit -> THOUSAND_LITRE
                else -> throw IllegalArgumentException("Invalid value for the calibration identification: $responseBit")
            }
        }
    }
}