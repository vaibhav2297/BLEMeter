package com.example.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.designsystem.theme.AppTheme

@Immutable
open class AppButtonColors internal constructor(
    private val activeContainerColor: Color,
    private val activeContentColor: Color,
    private val enabledContainerColor: Color,
    private val enabledContentColor: Color,
    private val disabledContainerColor: Color,
    private val disabledContentColor: Color
) {

    @Composable
    internal fun containerColor(buttonState: ButtonState): State<Color> {
        val color = when (buttonState) {
            ButtonState.ACTIVE -> activeContainerColor
            ButtonState.ENABLED -> enabledContainerColor
            ButtonState.DISABLED -> disabledContainerColor
        }
        return rememberUpdatedState(color)
    }

    @Composable
    internal fun contentColor(buttonState: ButtonState): State<Color> {
        val color = when (buttonState) {
            ButtonState.ACTIVE -> activeContentColor
            ButtonState.ENABLED -> enabledContentColor
            ButtonState.DISABLED -> disabledContentColor
        }
        return rememberUpdatedState(color)
    }
}


object AppButtonColorsDefault {

    @Composable
    private fun appButtonColor(): AppButtonColors =
        AppButtonColors(
            activeContentColor = AppTheme.colors.brand,
            activeContainerColor = AppTheme.colors.brand,
            enabledContainerColor = AppTheme.colors.textHighlighted,
            enabledContentColor = AppTheme.colors.onBackground,
            disabledContainerColor = AppTheme.colors.textHighlighted,
            disabledContentColor = AppTheme.colors.textHighlighted
        )

    @Composable
    fun buttonColor(buttonState: ButtonState): ButtonColors =
        ButtonDefaults.buttonColors(
            containerColor = appButtonColor().containerColor(buttonState = buttonState).value,
            contentColor = appButtonColor().contentColor(buttonState = buttonState).value,
            disabledContentColor = appButtonColor().contentColor(buttonState = ButtonState.DISABLED).value,
            disabledContainerColor = appButtonColor().containerColor(buttonState = ButtonState.DISABLED).value
        )

    @Composable
    private fun appOutlinedButtonColor(): AppButtonColors =
        AppButtonColors(
            activeContentColor = AppTheme.colors.brand,
            activeContainerColor = Color.Transparent,
            enabledContentColor = AppTheme.colors.onBackground,
            enabledContainerColor = Color.Transparent,
            disabledContentColor = AppTheme.colors.textHighlighted,
            disabledContainerColor = Color.Transparent,
        )

    @Composable
    fun outlinedButtonColor(buttonState: ButtonState): ButtonColors =
        ButtonDefaults.buttonColors(
            containerColor = appOutlinedButtonColor().containerColor(buttonState = buttonState).value,
            contentColor = appOutlinedButtonColor().contentColor(buttonState = buttonState).value,
            disabledContentColor = appOutlinedButtonColor().contentColor(buttonState = ButtonState.DISABLED).value,
            disabledContainerColor = appOutlinedButtonColor().containerColor(buttonState = ButtonState.DISABLED).value
        )

    @Composable
    fun borderStoke(
        buttonState: ButtonState,
        strokeWidth: Dp = 1.dp
    ): BorderStroke {
        val color = when (buttonState) {
            ButtonState.ACTIVE -> AppTheme.colors.brand
            else -> AppTheme.colors.stroke
        }

        return BorderStroke(
            width = strokeWidth,
            color = color,
        )
    }
}