package com.example.blemeter.core.ble.domain.model.request

import com.example.blemeter.model.MeterAddress
import com.example.blemeter.model.MeterType

/**
 * Class represents the base request that contains in every command.
 */
data class BaseRequest(
    override val meterAddress: String = ""
) : Base {
}

interface Base {
    val meterAddress: String
}