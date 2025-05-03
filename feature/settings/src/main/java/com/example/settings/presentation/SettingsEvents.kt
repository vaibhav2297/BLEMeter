package com.example.settings.presentation

sealed class SettingsEvents {

    data object OnLogout : SettingsEvents()

    data class OnLogoutAlert(val show: Boolean) : SettingsEvents()

    data class OnWallet(val navigate: Boolean) : SettingsEvents()

    data class OnCostConfigurationDialog(val show: Boolean) : SettingsEvents()

    data class OnCostConfiguration(val literPerRupees: Double) : SettingsEvents()

    data class OnNavigateToAuth(val navigate: Boolean) : SettingsEvents()
}