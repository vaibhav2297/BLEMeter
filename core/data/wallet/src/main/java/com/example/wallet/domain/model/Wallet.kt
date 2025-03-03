package com.example.wallet.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Wallet(
    @SerialName("created_at")
    val createdAt: String,

    @SerialName("balance")
    val balance: Double,

    @SerialName("id")
    val id: String,

    @SerialName("updated_at")
    val updatedAt: String,

    @SerialName("user_id")
    val userId: String
)