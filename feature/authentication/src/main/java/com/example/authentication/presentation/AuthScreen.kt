package com.example.authentication.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.authentication.components.AuthScreenSlot
import com.example.designsystem.theme.ValueChanged
import com.example.designsystem.theme.VoidCallback

@Composable
internal fun AuthRoute(
    viewModel: AuthViewModel = hiltViewModel(),
    onAuthenticated: VoidCallback
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AuthScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onAuthenticated = onAuthenticated
    )
}

@Composable
private fun AuthScreen(
    modifier: Modifier = Modifier,
    uiState: AuthUiState,
    onEvent: ValueChanged<AuthUiEvent>,
    onAuthenticated: VoidCallback
) {
    AuthScreenSlot(
        modifier = modifier,
        authType = uiState.authType
    ) { request ->
        onEvent(
            AuthUiEvent.OnAuthRequest(request)
        )
    }
}