package com.example.blemeter.core.ble.domain.model.request

data class MeterDataRequest(
    val baseRequest: BaseRequest = BaseRequest(),
    val spare: Int = 0
) : Base by baseRequest
