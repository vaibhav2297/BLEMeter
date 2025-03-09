package com.example.designsystem.components.textfield

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

abstract class BaseTextFieldInputState(
    val hint: String,
    initialText: String
) {
    var text by mutableStateOf(initialText)
        private set

    var hasError by mutableStateOf(false)
        private set

    fun updateText(newText: String) {
        text = newText
        hasError = validator()
    }

    protected abstract fun validator(): Boolean

    val isHint: Boolean
        get() = text == hint

}