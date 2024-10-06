package com.example.authentication.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SignUpError(
    val code: Int,
    val errorCode: String,
    val msg: String
)
