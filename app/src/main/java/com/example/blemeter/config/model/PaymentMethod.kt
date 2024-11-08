package com.example.blemeter.config.model

import com.example.blemeter.config.extenstions.getBit

enum class PaymentMethod(
    val commandBit: UInt,
    val responseBit: UInt
) {
    NONE(
        commandBit = 255u,
        responseBit = 255u
    ),

    ORDINARY(
        commandBit = 0x00u,
        responseBit = 0b0u
    ),

    STEP(
        commandBit = 0x4au,
        responseBit = 0b1u
    );

    companion object {
        fun getPaymentMethodByCommandBit(commandBit: UInt): PaymentMethod {
            return when (commandBit) {
                ORDINARY.commandBit -> ORDINARY
                STEP.commandBit -> STEP
                else -> throw IllegalArgumentException("Invalid value for the payment method: $commandBit")
            }
        }

        fun getPaymentMethodByResponseBit(responseBit: UByte): PaymentMethod {
            return when (responseBit.getBit(3)) {
                ORDINARY.responseBit -> ORDINARY
                STEP.responseBit -> STEP
                else -> throw IllegalArgumentException("Invalid value for the payment method: $responseBit")
            }
        }
    }
}