package com.example.authentication.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AppMetadata(
    val provider: String,
    val providers: List<String>
)