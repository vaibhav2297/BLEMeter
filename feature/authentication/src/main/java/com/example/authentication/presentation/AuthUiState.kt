package com.example.authentication.presentation

import com.example.authentication.model.AuthType
import com.example.designsystem.components.ScreenState


internal data class AuthUiState(
    val authType: AuthType = AuthType.LOGIN_WITH_EMAIL,
    val authState: ScreenState<Unit> = ScreenState.None
)
