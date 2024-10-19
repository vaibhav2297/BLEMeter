package com.example.wallet.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.designsystem.utils.ScreenState
import com.example.local.datastore.DataStoreKeys
import com.example.local.datastore.IAppDataStore
import com.example.local.model.UserEntity
import com.example.payment.domain.PaymentOptions
import com.example.payment.domain.Prefill
import com.example.payments.domain.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class WalletViewModel @Inject constructor(
    private val paymentRepository: PaymentRepository,
    private val dataStore: IAppDataStore
) : ViewModel() {

    private val _uiState: MutableStateFlow<WalletUiState> by lazy {
        MutableStateFlow(WalletUiState())
    }

    val uiState = _uiState.asStateFlow()

    init {
        getUserBalance()
    }

    private fun getUserBalance() {
        viewModelScope.launch {
            val userId = dataStore.getPreference(DataStoreKeys.USER_ID_KEY, "").firstOrNull()
            paymentRepository
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

   private fun showLoading() {
       _uiState.update {
           it.copy(
               state = ScreenState.Loading
           )
       }
   }

}