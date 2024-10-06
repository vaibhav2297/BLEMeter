package com.example.authentication.presentation

import com.example.authentication.model.AuthType

internal data class AuthUiState(
    val authType: AuthType = AuthType.LOGIN_WITH_EMAIL
)
