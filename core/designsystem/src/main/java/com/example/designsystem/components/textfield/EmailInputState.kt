package com.example.designsystem.components.textfield

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.setValue

class EmailInputState(
    hint: String,
    initialText: String
) : BaseTextFieldInputState(hint, initialText) {

    override fun validator(): Boolean =
        text.matches(Regex(EMAIL_REGEX))

    companion object {
        const val EMAIL_REGEX = "\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*"
    }
}

@Composable
fun rememberEmailInputState(
    hint: String,
    initialText: String = ""
): EmailInputState =
    remember(
        hint,
        initialText
    ) {
        EmailInputState(
            hint = hint,
            initialText = initialText
        )
    }