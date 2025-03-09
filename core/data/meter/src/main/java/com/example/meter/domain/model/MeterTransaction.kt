package com.example.meter.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeterTransaction(

    @SerialName("id")
    val id: String,

    @SerialName("meter_id")
    val meterId: String,

    @SerialName("amount")
    val amount: Double,

    @SerialName("user_id")
    val userId: Long,

    @SerialName("purchase_times")
    val purchaseTimes: String,

    @SerialName("created_at")
    val createdAt: String
)
