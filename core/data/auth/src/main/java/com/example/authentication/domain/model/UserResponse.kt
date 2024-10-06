package com.example.authentication.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(

    @SerialName("app_metadata")
    val appMetadata: AppMetadata,


    val aud: String,

    @SerialName("confirmation_sent_at")
    val confirmationSentAt: String,

    @SerialName("created_at")
    val createdAt: String,

    val email: String,

    val id: String,

    val identities: List<String>,

    @SerialName("is_anonymous")
    val isAnonymous: Boolean,

    val phone: String,

    val role: String,

    @SerialName("updated_at")
    val updatedAt: String,

    @SerialName("user_metadata")
    val userMetadata: UserMetadata
)