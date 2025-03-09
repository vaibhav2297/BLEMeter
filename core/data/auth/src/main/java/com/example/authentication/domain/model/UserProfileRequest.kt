package com.example.authentication.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileRequest(

    @SerialName("user_id")
    val userId: String,

    @SerialName("is_admin")
    val isAdmin: Boolean
)
