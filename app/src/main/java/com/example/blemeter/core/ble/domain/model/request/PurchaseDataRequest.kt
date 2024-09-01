package com.example.blemeter.core.ble.domain.model.request

data class PurchaseDataRequest(
    val numberTimes: UInt = 0u,
    val purchaseVariable: UInt = 0u
)
