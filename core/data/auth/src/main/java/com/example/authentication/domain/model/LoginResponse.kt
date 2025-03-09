package com.example.authentication.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse (
    @SerialName("access_token")
    val accessToken: String,

    @SerialName("token_type")
    val tokenType: String,

    @SerialName("expires_in")
    val expiresIn: Long,

    @SerialName("expires_at")
    val expiresAt: Long,

    @SerialName("refresh_token")
    val refreshToken: String,

    val user: UserResponse
)