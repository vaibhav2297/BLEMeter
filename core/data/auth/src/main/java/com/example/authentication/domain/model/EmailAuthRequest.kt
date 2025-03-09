package com.example.authentication.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class EmailAuthRequest(
    val email: String,
    val password: String,
    val isAdmin: Boolean = false
)
