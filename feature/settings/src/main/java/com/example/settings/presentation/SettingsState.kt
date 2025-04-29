package com.example.settings.presentation

import com.example.designsystem.utils.ScreenState

internal data class SettingsState(
    val screenState: ScreenState<Unit> = ScreenState.None,
    val walletBalance: Double = 0.0,
    val showLogoutAlert: Boolean = false,
    val navigateToWallet: Boolean = false,
    val navigateToAuth: Boolean = false
)