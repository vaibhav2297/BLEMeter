package com.example.blemeter.config.model

import com.example.blemeter.core.ble.domain.model.DataIdentifier

data class NumberingInstructionData(
    val calibrationIdentification: CalibrationIdentification,
    val inPlaceMethod: InPlaceMethod
) : Data {
    override val dataIdentifier: DataIdentifier
        get() = DataIdentifier.NUMBERING_INSTRUCTION
}
