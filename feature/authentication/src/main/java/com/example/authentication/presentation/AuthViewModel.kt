package com.example.authentication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentication.domain.model.EmailAuthRequest
import com.example.authentication.domain.repository.IAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AuthViewModel @Inject constructor(
    private val authRepo: IAuthRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<AuthUiState> by lazy {
        MutableStateFlow(AuthUiState())
    }
    val uiState = _uiState.asStateFlow()


    fun onEvent(event: AuthUiEvent) {
        when(event) {
            is AuthUiEvent.OnAuthRequest -> {}
            is AuthUiEvent.OnAuthType -> {}
        }
    }

    private fun signUpWithEmail(
        request: EmailAuthRequest
    ) {
        viewModelScope.launch {
            authRepo.signUpWithEmail(request)
                .onSuccess {  }
                .onFailure {  }
        }
    }

    private fun loginWithEmail(
        request: EmailAuthRequest
    ) {
        viewModelScope.launch {
            authRepo.loginWithEmail(request)
                .onSuccess {  }
                .onFailure {  }
        }
    }
}