package com.example.blemeter.config.model

data class ProductVersion(
    val inPlaceMethod: InPlaceMethod = InPlaceMethod.NONE,
    val calibrationIdentification: CalibrationIdentification = CalibrationIdentification.NONE,
    val paymentMethod: PaymentMethod = PaymentMethod.NONE
) {

    companion object {

        fun getProductVersionFromResponseBit(value: UInt): ProductVersion {
            val inPlaceMethod = InPlaceMethod.getInPlaceMethodByResponseBit(value.toUByte())
            val paymentMethod = PaymentMethod.getPaymentMethodByResponseBit(value.toUByte())
            val calibrationIdentification = CalibrationIdentification.getCalibrationIdentificationByResponseBit(value.toUByte())

            return ProductVersion(
                inPlaceMethod = inPlaceMethod,
                paymentMethod = paymentMethod,
                calibrationIdentification = calibrationIdentification
            )
        }

        fun getProductVersionFromCommandBit(value: UInt): ProductVersion {
            val inPlaceMethod = InPlaceMethod.getInPlaceMethodByCommandBit(value)
            val paymentMethod = PaymentMethod.getPaymentMethodByCommandBit(value)
            val calibrationIdentification = CalibrationIdentification.getCalibrationIdentificationByCommandBit(value)

            return ProductVersion(
                inPlaceMethod = inPlaceMethod,
                paymentMethod = paymentMethod,
                calibrationIdentification = calibrationIdentification
            )
        }
    }

}
