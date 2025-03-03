package com.example.wallet.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WalletTransaction(
    @SerialName("amount")
    val amount: Double,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("id")
    val id: Int,

    @SerialName("meter_id")
    val meterId: String,

    @SerialName("purchase_frequency")
    val purchaseFrequency: Int,

    @SerialName("transaction_type")
    val transactionType: String,

    @SerialName("user_id")
    val userId: String,

    @SerialName("wallet_id")
    val walletId: String
)
