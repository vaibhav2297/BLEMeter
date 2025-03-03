package com.example.meter.domain.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeterTransactionRequest(
    @SerialName("meter_id")
    val meterId: String,

    val amount: Double,

    @SerialName("user_id")
    val userId: String,

    @SerialName("purchase_times")
    val purchaseTimes: Int
)
