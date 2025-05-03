package com.example.authentication.domain.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileRequest(

    @SerialName("user_id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val userId: String?,

    @SerialName("is_admin")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val isAdmin: Boolean?,

    @SerialName("liters_per_rupees")
    val litersPerRupees: Double = 0.0
)
