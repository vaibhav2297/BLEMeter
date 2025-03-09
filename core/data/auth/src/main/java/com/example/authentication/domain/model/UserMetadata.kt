package com.example.authentication.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserMetadata(

    val email: String? = null,

    @SerialName("email_verified")
    val isEmailVerified: Boolean? = null,

    @SerialName("phone_verified")
    val isPhoneVerified: Boolean? = null,

    val sub: String? = null
)