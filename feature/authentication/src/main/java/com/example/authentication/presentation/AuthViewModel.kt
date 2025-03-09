package com.example.authentication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentication.domain.model.EmailAuthRequest
import com.example.authentication.domain.model.UserResponse
import com.example.authentication.domain.repository.IAuthRepository
import com.example.authentication.model.AuthType
import com.example.designsystem.utils.ScreenState
import com.example.local.datastore.DataStoreKeys
import com.example.local.datastore.IAppDataStore
import com.example.wallet.domain.model.Wallet
import com.example.wallet.domain.model.WalletResponse
import com.example.wallet.domain.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AuthViewModel @Inject constructor(
    private val authRepo: IAuthRepository,
    private val walletRepo: WalletRepository,
    private val dataStore: IAppDataStore
) : ViewModel() {

    private val _uiState: MutableStateFlow<AuthUiState> by lazy {
        MutableStateFlow(AuthUiState())
    }
    val uiState = _uiState.asStateFlow()

    init {
        checkAuthentication()
    }

    fun onEvent(event: AuthUiEvent) {
        when (event) {
            is AuthUiEvent.OnAuthRequest -> initiateAuthRequest(event.request)
            is AuthUiEvent.OnAuthChange -> updateAuthType(event.authType)
        }
    }

    private fun checkAuthentication() {
        viewModelScope.launch {
            showLoading()
            val isLoggedIn =
                dataStore.getPreference(DataStoreKeys.USER_LOGGED_IN_KEY, false).firstOrNull()
                    ?: false

            if (isLoggedIn) {
                _uiState.update {
                    it.copy(
                        authState = ScreenState.Success(Unit)
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        authState = ScreenState.Error("Login")
                    )
                }
            }
        }
    }

    private fun initiateAuthRequest(authRequest: EmailAuthRequest) {
        showLoading()
        when (_uiState.value.authType) {
            AuthType.LOGIN_WITH_EMAIL -> loginWithEmail(authRequest)
            AuthType.SIGNUP_WITH_EMAIL -> signUpWithEmail(authRequest)
        }
    }

    private fun updateAuthType(authType: AuthType) {
        _uiState.update { it.copy(authType = authType) }
    }

    private fun signUpWithEmail(
        request: EmailAuthRequest
    ) {
        viewModelScope.launch {
            authRepo.signUpWithEmail(request)
                .onSuccess { response ->


                    //store to local data
                    storeAuthToken(
                        authToken = response.accessToken,
                        refreshToken = response.refreshToken
                    )

                    storeUserInfo(response.user)

                    _uiState.update {
                        it.copy(
                            authState = ScreenState.Success(Unit)
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(authState = ScreenState.Error(e.message ?: "Unknown Error"))
                    }
                }
        }
    }

    private fun loginWithEmail(
        request: EmailAuthRequest
    ) {
        viewModelScope.launch {
            authRepo.loginWithEmail(request)
                .onSuccess { response ->

                    //store to shared pref
                    storeAuthToken(
                        authToken = response.accessToken,
                        refreshToken = response.refreshToken
                    )

                    storeUserInfo(response.user)

                    //user wallet info
                    getUserWallet()?.let { wallet -> storeUserWallet(wallet) }

                    _uiState.update {
                        it.copy(
                            authState = ScreenState.Success(Unit)
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(authState = ScreenState.Error(e.message ?: "Unknown Error"))
                    }
                }
        }
    }

    private suspend fun getUserWallet() =
        walletRepo.getWallet().getOrNull()?.firstOrNull()

    private suspend fun storeAuthToken(authToken: String, refreshToken: String) {
        dataStore.apply {
            putPreference(DataStoreKeys.AUTH_TOKEN_KEY, authToken)
            putPreference(DataStoreKeys.REFRESH_TOKEN_KEY, refreshToken)
        }
    }

    private suspend fun storeUserInfo(user: UserResponse) {
        dataStore.apply {
            putPreference(DataStoreKeys.USER_ID_KEY, user.id)
            putPreference(DataStoreKeys.USER_LOGGED_IN_KEY, true)
        }
    }

    private suspend fun storeUserWallet(wallet: Wallet) {
        dataStore.apply {
            putPreference(DataStoreKeys.USER_WALLET_ID, wallet.id)
        }
    }

    private fun showLoading() {
        _uiState.update {
            it.copy(
                authState = ScreenState.Loading
            )
        }
    }
}