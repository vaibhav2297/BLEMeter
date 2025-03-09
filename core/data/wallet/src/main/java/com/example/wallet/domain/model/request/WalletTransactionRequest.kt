package com.example.wallet.domain.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WalletTransactionRequest(

    @SerialName("amount")
    val amount: Double,

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
