package com.example.authentication.domain.model

import kotlinx.serialization.SerialName

data class RefreshTokenRequest(
    @SerialName("refresh_token")
    val refreshToken: String,
)