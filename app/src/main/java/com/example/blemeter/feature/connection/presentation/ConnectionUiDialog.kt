package com.example.blemeter.feature.connection.presentation

sealed interface ConnectionUiDialog {

    data object None : ConnectionUiDialog

    data object BluetoothEnableDialog : ConnectionUiDialog

    data class ErrorDialog(val error: String) : ConnectionUiDialog
}