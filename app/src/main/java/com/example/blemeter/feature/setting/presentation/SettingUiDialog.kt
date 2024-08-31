package com.example.blemeter.feature.setting.presentation

sealed interface SettingUiDialog {

    data object None : SettingUiDialog

    data object EditMeterAddressDialog : SettingUiDialog
}