package com.example.blemeter.config.model

import com.example.blemeter.core.ble.domain.model.DataIdentifier

data class ValveControlData(
    val controlState: ControlState = NoneStatus.NONE
) : Data {
    override val dataIdentifier: DataIdentifier
        get() = DataIdentifier.VALVE_CONTROL_DATA
}
