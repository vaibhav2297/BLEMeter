package com.example.blemeter.model

import com.example.blemeter.core.ble.domain.model.DataIdentifier

data class NoData(
    val spare: Int = 0
) : Data {
    override val dataIdentifier: DataIdentifier
        get() = DataIdentifier.NONE
}
