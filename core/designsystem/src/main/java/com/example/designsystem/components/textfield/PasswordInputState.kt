package com.example.designsystem.components.textfield

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.setValue

class PasswordInputState(
    hint: String,
    initialText: String
) : BaseTextFieldInputState(hint, initialText) {

    override fun validator(): Boolean =
        text.isEmpty() || text.length < 4
}

@Composable
fun rememberPasswordInputState(
    hint: String,
    initialText: String = ""
): PasswordInputState =
    remember(
        hint,
        initialText
    ) {
        PasswordInputState(
            hint = hint,
            initialText = initialText
        )
    }