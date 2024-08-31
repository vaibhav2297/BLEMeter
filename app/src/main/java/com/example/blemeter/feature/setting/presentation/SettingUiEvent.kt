package com.example.blemeter.feature.setting.presentation

sealed interface SettingUiEvent {

    data class OnMeterAddressEdit(val isEditing: Boolean) : SettingUiEvent

    data class OnMeterAddressSave(val address: String) : SettingUiEvent

    data class OnDialog(val dialog: SettingUiDialog) : SettingUiEvent
}