package com.example.wallet.domain.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WalletRequest(

    @SerialName("balance")
    val balance: Double,

    @SerialName("user_id")
    val userId: String
)