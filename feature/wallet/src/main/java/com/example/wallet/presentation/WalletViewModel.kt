package com.example.wallet.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.designsystem.utils.ScreenState
import com.example.local.datastore.DataStoreKeys
import com.example.local.datastore.IAppDataStore
import com.example.logger.ExceptionHandler
import com.example.logger.ILogger
import com.example.wallet.domain.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class WalletViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
    private val dataStore: IAppDataStore,
    private val logger: ILogger,
    private val exceptionHandler: ExceptionHandler
) : ViewModel() {

    private val _uiState: MutableStateFlow<WalletUiState> by lazy {
        MutableStateFlow(WalletUiState())
    }

    val uiState = _uiState.asStateFlow()

    init {
        getUserBalance()
        fetchWalletBalance()
    }

    private fun getUserBalance() {
        viewModelScope.launch {
            val userId = dataStore.getPreference(DataStoreKeys.USER_ID_KEY, "").firstOrNull()
            walletRepository
                .getUserWalletBalance(userId = userId ?: "")
                .collect { amount ->
                    _uiState.update {
                        it.copy(
                            amount = amount
                        )
                    }
                }
        }
    }

    private fun fetchWalletBalance() {
        viewModelScope.launch {
            showLoading()
            val userId = dataStore.getPreference(DataStoreKeys.USER_ID_KEY, "").firstOrNull()
            logger.d("fetchWalletBalance :: for $userId")
            walletRepository
                .getWallet(userId = userId ?: "")
                .onSuccess {
                    logger.d("fetchWalletBalance :: success")
                    _uiState.update {
                        it.copy(
                            state = ScreenState.Success(Unit)
                        )
                    }
                }
                .onFailure { e->
                    logger.d("fetchWalletBalance :: error")
                    _uiState.update {
                        it.copy(
                            state = ScreenState.Error(e.message ?: "")
                        )
                    }
                }
        }
    }

   private fun showLoading() {
       _uiState.update {
           it.copy(
               state = ScreenState.Loading
           )
       }
   }

}