package com.example.blemeter.feature.recharge.presentation

import com.example.designsystem.utils.ScreenState

data class RechargeUiState(
    val numberOfTimes: Int = 0,
    val rechargeAmount: Double = 0.0,
    val currentAmount: Double = 0.0,
    val screenState: ScreenState<Unit> = ScreenState.None
)
