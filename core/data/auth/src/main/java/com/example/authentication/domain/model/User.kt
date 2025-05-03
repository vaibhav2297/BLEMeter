package com.example.authentication.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(

    @SerialName("user_id")
    val userId: String,

    @SerialName("is_admin")
    val isAdmin: Boolean,

    @SerialName("liters_per_rupees")
    val litersPerRupees: Boolean,

    @SerialName("app_metadata")
    val appMetadata: AppMetadata,

    val aud: String,

    @SerialName("confirmation_sent_at")
    val confirmationSentAt: String = "",

    val email: String,

    val identities: List<Identity> = emptyList(),

    @SerialName("is_anonymous")
    val isAnonymous: Boolean,

    val phone: String = "",

    val role: String = "",

    @SerialName("updated_at")
    val updatedAt: String = "",

    @SerialName("created_at")
    val createdAt: String,
)
