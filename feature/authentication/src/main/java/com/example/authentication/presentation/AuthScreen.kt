package com.example.authentication.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.authentication.components.AuthScreenSlot
import com.example.designsystem.components.AppScaffold
import com.example.designsystem.theme.ValueChanged
import com.example.designsystem.theme.VoidCallback
import com.example.designsystem.utils.isSuccess

@Composable
internal fun AuthRoute(
    viewModel: AuthViewModel = hiltViewModel(),
    onAuthenticated: VoidCallback
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.authState.isSuccess()) {
        onAuthenticated()
    }

    AuthScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun AuthScreen(
    modifier: Modifier = Modifier,
    uiState: AuthUiState,
    onEvent: ValueChanged<AuthUiEvent>
) {
    AppScaffold(
        screenState = uiState.authState
    ) {
        AuthScreenSlot(
            modifier = modifier,
            authType = uiState.authType,
            onAuthChange = { authType ->
                onEvent(
                    AuthUiEvent.OnAuthChange(authType)
                )
            }
        ) { request ->
            onEvent(
                AuthUiEvent.OnAuthRequest(request)
            )
        }
    }
}