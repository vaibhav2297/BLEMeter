package com.example.user.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(

    @SerialName("user_id")
    val userId: String = "",

    @SerialName("is_admin")
    val isAdmin: Boolean = false,

    @SerialName("liters_per_rupees")
    val litersPerRupees: Double = 0.0
)
