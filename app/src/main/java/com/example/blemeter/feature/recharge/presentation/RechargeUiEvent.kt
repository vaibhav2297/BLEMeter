package com.example.blemeter.feature.recharge.presentation

sealed interface RechargeUiEvent {

    data object OnRecharge : RechargeUiEvent

    data class OnRechargeValueChanged(val amount: Double) : RechargeUiEvent
}