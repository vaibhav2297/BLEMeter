package com.example.blemeter.core.ble.domain.model.request

data class PurchaseDataRequest(
    val baseRequest: BaseRequest = BaseRequest(),
    val numberTimes: UInt = 0u,
    val purchaseVariable: UInt = 0u
) : Base by baseRequest
