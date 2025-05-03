package com.example.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentication.domain.model.UserProfileRequest
import com.example.authentication.domain.repository.IAuthRepository
import com.example.designsystem.utils.ScreenState
import com.example.local.datastore.DataStoreKeys
import com.example.local.datastore.IAppDataStore
import com.example.logger.ExceptionHandler
import com.example.logger.ILogger
import com.example.user.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    private val authRepo: IAuthRepository,
    private val logger: ILogger,
    private val dataStore: IAppDataStore,
    private val exceptionHandler: ExceptionHandler
) : ViewModel() {

    private val _uiState: MutableStateFlow<SettingsState> by lazy {
        MutableStateFlow(SettingsState())
    }
    val uiState = _uiState.asStateFlow()

    init {
        //getCostConfiguration()
    }

    fun onEvent(event: SettingsEvents) {
        when (event) {
            is SettingsEvents.OnLogout -> logout()
            is SettingsEvents.OnWallet -> navigateToWallet(event.navigate)
            is SettingsEvents.OnNavigateToAuth -> navigateToAuth(event.navigate)
            is SettingsEvents.OnLogoutAlert -> showLogoutAlert(event.show)
            is SettingsEvents.OnCostConfigurationDialog -> showCostConfigDialog(event.show)
            is SettingsEvents.OnCostConfiguration -> onConstConfiguration(event.literPerRupees)
        }
    }

    private fun getCostConfiguration() {
        viewModelScope.launch {
            logger.d("getCostConfiguration")
            updateScreenState(ScreenState.Loading)

            authRepo.getUserProfile()
                .onSuccess { userProfile ->
                    logger.d("getCostConfiguration :: success :: $userProfile")
                    dataStore.putPreference(DataStoreKeys.COST_CONFIGURATION_KEY, 0.0)

                    _uiState.update {
                        it.copy(
                            literPerRupees = userProfile.litersPerRupees
                        )
                    }
                    updateScreenState(ScreenState.Success(Unit))
                }
                .onFailure { e ->
                    exceptionHandler.handle(e)
                    updateScreenState(ScreenState.Error(e.message ?: ""))
                }

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

    private fun onConstConfiguration(literPerRupees: Double) {
        viewModelScope.launch {

            logger.d("onConstConfiguration :: $literPerRupees")

            showCostConfigDialog(false)
            updateScreenState(ScreenState.Loading)

            val userId = dataStore.getPreference(DataStoreKeys.USER_ID_KEY, "").first()

            authRepo.updateUserProfile(
                request = UserProfileRequest(
                    litersPerRupees = literPerRupees,
                    isAdmin = null,
                    userId = userId
                )
            ).onSuccess {
                logger.d("onConstConfiguration :: onSuccess")

                dataStore.putPreference(DataStoreKeys.COST_CONFIGURATION_KEY, literPerRupees)

                updateScreenState(ScreenState.Success(Unit))

            }.onFailure { e ->
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

    private fun showCostConfigDialog(show: Boolean) {
        if (show){
            getCostConfiguration()
        }

        _uiState.update {
            it.copy(
                showCostConfigDialog = show
            )
        }
    }

    private fun updateScreenState(state: ScreenState<Unit>) {
        _uiState.update {
            it.copy(
                screenState = state
            )
        }
    }
}