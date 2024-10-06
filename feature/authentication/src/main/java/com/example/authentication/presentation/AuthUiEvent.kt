package com.example.authentication.presentation

import com.example.authentication.domain.model.EmailAuthRequest
import com.example.authentication.model.AuthType

internal sealed interface AuthUiEvent {

    data class OnAuthType(val authType: AuthType): AuthUiEvent

    data class OnAuthRequest(val request: EmailAuthRequest): AuthUiEvent
}