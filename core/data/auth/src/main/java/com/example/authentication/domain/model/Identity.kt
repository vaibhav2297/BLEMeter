package com.example.authentication.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Identity (
    @SerialName("identity_id")
    val identityID: String,

    val id: String,

    @SerialName("user_id")
    val userID: String,

    @SerialName("identity_data")
    val identityData: Data,

    val provider: String,

    @SerialName("last_sign_in_at")
    val lastSignInAt: String,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("updated_at")
    val updatedAt: String,

    val email: String
)

@Serializable
data class Data (
    val email: String,

    @SerialName("email_verified")
    val emailVerified: Boolean,

    @SerialName("phone_verified")
    val phoneVerified: Boolean,

    val sub: String
)