package com.example.blemeter.feature.setting.presentation

data class SettingUiState(
    val meterAddress: String = "",
    val isEditing: Boolean = false,
    val dialog: SettingUiDialog = SettingUiDialog.None
)
