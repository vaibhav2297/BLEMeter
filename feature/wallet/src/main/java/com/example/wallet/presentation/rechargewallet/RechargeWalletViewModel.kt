package com.example.wallet.presentation.rechargewallet

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.designsystem.utils.ScreenState
import com.example.local.datastore.DataStoreKeys
import com.example.local.datastore.IAppDataStore
import com.example.local.model.UserEntity
import com.example.logger.ExceptionHandler
import com.example.logger.ILogger
import com.example.payment.PaymentActivity
import com.example.payment.domain.PaymentOptions
import com.example.payment.domain.Prefill
import com.example.wallet.domain.model.TransactionType
import com.example.wallet.domain.model.request.WalletTransactionRequest
import com.example.wallet.domain.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class RechargeWalletViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
    private val dataStore: IAppDataStore,
    private val exceptionHandler: ExceptionHandler,
    private val logger: ILogger
) : ViewModel() {

    private val _uiState: MutableStateFlow<RechargeWalletUiState> by lazy {
        MutableStateFlow(RechargeWalletUiState())
    }

    val uiState = _uiState.asStateFlow()

    fun onEvent(event: RechargeWalletEvents) {
        when (event) {
            is RechargeWalletEvents.OnRechargeAmountChange -> onRechargeAmountChange(event.amount)
            RechargeWalletEvents.OnRecharge -> initiateRecharge()
            RechargeWalletEvents.OnNavigatedToPayment -> navigatedToPayment()
            is RechargeWalletEvents.OnPaymentResult -> handlePaymentResult(event.data)
        }
    }

    private fun onRechargeAmountChange(amount: Double) {
        _uiState.update {
            it.copy(
                rechargeAmount = amount
            )
        }
    }

    private fun initiateRecharge() {
        viewModelScope.launch {
            getUser()?.let { user ->
                val paymentOptions = PaymentOptions(
                    prefill = Prefill(
                        email = user.email,
                        contact = "+919821216693"
                    ),
                    description = "Wallet Recharge",
                    amount = (_uiState.value.rechargeAmount * 100).toInt()
                )

                _uiState.update {
                    it.copy(
                        navigateToPayment = true,
                        paymentOptions = paymentOptions
                    )
                }
            }
        }
    }

    private fun navigatedToPayment() {
        _uiState.update {
            it.copy(
                navigateToPayment = false
            )
        }
    }

    private fun handlePaymentResult(data: Intent) {
        viewModelScope.launch(Dispatchers.IO) {
            showLoading()
            val isSucceed = data.getBooleanExtra(PaymentActivity.IS_PAYMENT_SUCCEED, false)

            logger.d( "handlePaymentResult: isSucceed: $isSucceed")
            if (isSucceed.not()) {
                val errorCode = data.getIntExtra(PaymentActivity.PAYMENT_ERROR_CODE, -1)
                logger.e( "handlePaymentResult: errorCode: $errorCode")
                _uiState.update {
                    it.copy(
                        state = ScreenState.Error(error = "Payment error : $errorCode")
                    )
                }
            } else {
                insertWalletTransaction()

                _uiState.update {
                    it.copy(
                        state = ScreenState.Success(Unit)
                    )
                }
            }
        }
    }

    private fun updateWalletBalance() {
        viewModelScope.launch {
            val userId = dataStore.getPreference(DataStoreKeys.USER_ID_KEY, "").firstOrNull() ?: ""
            walletRepository.updateWalletAmount(
                userId = userId,
                amount = _uiState.value.rechargeAmount,
                transactionType = TransactionType.CREDIT
            )
        }
    }

    private suspend fun insertWalletTransaction() {
        try {
            val userId =
                dataStore.getPreference(DataStoreKeys.USER_ID_KEY, "").firstOrNull() ?: ""

            val meterId =
                dataStore.getPreference(DataStoreKeys.METER_ADDRESS_KEY, "").firstOrNull() ?: ""

            val walletId = walletRepository.getWalletId(userId)

            val request = WalletTransactionRequest(
                userId = userId,
                meterId = meterId,
                amount = _uiState.value.rechargeAmount,
                purchaseFrequency = 0,
                transactionType = TransactionType.CREDIT.name,
                walletId = walletId
            )

            logger.d( "RechargeWalletViewmodel: walletTransactionRequest: $request")

            walletRepository.insertWalletTransaction(request)
                .onSuccess { logger.d( "RechargeWalletViewmodel :: insertWalletTransaction: success") }
                .onFailure { e -> exceptionHandler.handle(Exception(e)) }
        } catch (e: Exception) {
            logger.e("Error :: ${e.message}")
            exceptionHandler.handle(e)
        }
    }

    private suspend fun getUser(): UserEntity? {
        val userId = dataStore.getPreference(DataStoreKeys.USER_ID_KEY, "").firstOrNull() ?: ""
        return walletRepository.getUser(userId)
    }

    private fun showLoading() {
        _uiState.update {
            it.copy(
                state = ScreenState.Loading
            )
        }
    }
}