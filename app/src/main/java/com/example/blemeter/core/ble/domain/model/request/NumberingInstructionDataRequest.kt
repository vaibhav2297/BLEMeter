package com.example.blemeter.core.ble.domain.model.request

import com.example.blemeter.config.model.CalibrationIdentification
import com.example.blemeter.config.model.InPlaceMethod
import com.example.blemeter.config.model.PaymentMethod

data class NumberingInstructionDataRequest(
    val calibrationIdentification: CalibrationIdentification = CalibrationIdentification.NONE,
    val inPlaceMethod: InPlaceMethod = InPlaceMethod.NONE,
    val paymentMethod: PaymentMethod = PaymentMethod.NONE
)
