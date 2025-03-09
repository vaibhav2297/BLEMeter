package com.example.blemeter.core.ble.domain.model.request

data class PurchaseDataRequest(
    val numberTimes: Int = 0,
    val purchaseVariable: Double = 0.0
)
