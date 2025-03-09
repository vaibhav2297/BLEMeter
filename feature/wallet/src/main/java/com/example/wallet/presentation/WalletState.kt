package com.example.wallet.presentation

import com.example.designsystem.utils.ScreenState

internal data class WalletUiState(
    val state: ScreenState<Unit> = ScreenState.None,
    val amount: Double = 0.0
)