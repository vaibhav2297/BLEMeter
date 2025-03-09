package com.example.wallet.presentation.rechargewallet

import android.content.Intent

sealed interface RechargeWalletEvents {

    data object OnRecharge : RechargeWalletEvents

    data class OnRechargeAmountChange(val amount: Double) : RechargeWalletEvents

    data object OnNavigatedToPayment : RechargeWalletEvents

    data class OnPaymentResult(val data: Intent) : RechargeWalletEvents
}