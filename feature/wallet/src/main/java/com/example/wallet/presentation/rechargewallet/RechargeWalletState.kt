package com.example.wallet.presentation.rechargewallet

import com.example.designsystem.utils.ScreenState
import com.example.payment.domain.PaymentOptions


internal data class RechargeWalletUiState(
    val state: ScreenState<Unit> = ScreenState.None,
    val rechargeAmount: Double = 0.0,
    val navigateToPayment: Boolean = false,
    val paymentOptions: PaymentOptions = PaymentOptions(amount = 0)
)