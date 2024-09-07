package com.example.blemeter.ui.components.textfield

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.setValue

class TextFieldInputState(
    val hint: String,
    val initialText: String
) {
    var text by mutableStateOf(initialText)
        private set

    fun updateText(newText: String) {
        text = newText
    }

    val isHint: Boolean
        get() = text == hint

    companion object {
        val Saver: Saver<TextFieldInputState, *> = listSaver(
            save = { listOf(it.hint, it.text) },
            restore = {
                TextFieldInputState(
                    hint = it[0],
                    initialText = it[1],
                )
            }
        )
    }

}

@Composable
fun rememberTextFieldInputState(
    hint: String,
    initialText: String = ""
): TextFieldInputState =
    remember(hint, initialText) {
        TextFieldInputState(
            hint = hint,
            initialText = initialText
        )
    }