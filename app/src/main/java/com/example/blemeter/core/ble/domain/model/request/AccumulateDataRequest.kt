package com.example.blemeter.core.ble.domain.model.request

data class AccumulateDataRequest(
    val baseRequest: BaseRequest = BaseRequest(),
    val accumulate: UInt
) : Base by baseRequest
