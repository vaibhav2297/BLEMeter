package com.example.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentication.domain.repository.IAuthRepository
import com.example.designsystem.utils.ScreenState
import com.example.logger.ExceptionHandler
import com.example.logger.ILogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    private val authRepo: IAuthRepository,
    private val logger: ILogger,
    private val exceptionHandler: ExceptionHandler
) : ViewModel() {

    private val _uiState: MutableStateFlow<SettingsState> by lazy {
        MutableStateFlow(SettingsState())
    }
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: SettingsEvents) {
        when (event) {
            is SettingsEvents.OnLogout -> logout()
            is SettingsEvents.OnWallet -> navigateToWallet(event.navigate)
            is SettingsEvents.OnNavigateToAuth -> navigateToAuth(event.navigate)
            is SettingsEvents.OnMeterConfiguration -> meterConfiguration()
            is SettingsEvents.OnLogoutAlert -> showLogoutAlert(event.show)
        }
    }

    private fun showLogoutAlert(show: Boolean) {
        _uiState.update {
            it.copy(
                showLogoutAlert = show
            )
        }
    }

    private fun logout() {
        viewModelScope.launch {
            updateScreenState(ScreenState.Loading)

            authRepo.logout()
                .onSuccess {
                    logger.d("Logged out!")
                    updateScreenState(ScreenState.Success(Unit))

                    navigateToAuth(true)
                }
                .onFailure { e ->
                    exceptionHandler.handle(e)
                    updateScreenState(ScreenState.Error(e.message ?: ""))
                }
        }
    }

    private fun navigateToWallet(navigate: Boolean) {
        _uiState.update {
            it.copy(
                navigateToWallet = navigate
            )
        }
    }

    private fun navigateToAuth(navigate: Boolean) {
        _uiState.update {
            it.copy(
                navigateToAuth = navigate
            )
        }
    }

    private fun meterConfiguration() {

    }

    private fun updateScreenState(state: ScreenState<Unit>) {
        _uiState.update {
            it.copy(
                screenState = state
            )
        }
    }
}