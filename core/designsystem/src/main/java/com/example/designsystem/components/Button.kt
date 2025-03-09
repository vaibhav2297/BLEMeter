package com.example.designsystem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.theme.MeterAppTheme
import com.example.designsystem.theme.VoidCallback

object AppButtonDefaults {

    val contentPadding: PaddingValues
        @Composable
        get() = PaddingValues(
            horizontal = AppTheme.padding.small,
            vertical = AppTheme.padding.large,
        )
}


enum class ButtonState {
    ENABLED,
    DISABLED,
    ACTIVE
}

@Composable
fun AppOutlinedButton(
    modifier: Modifier = Modifier,
    buttonState: ButtonState = ButtonState.ENABLED,
    text: String,
    shape: Shape = MaterialTheme.shapes.medium,
    strokeWidth: Dp = 1.dp,
    textStyle: TextStyle = MaterialTheme.typography.labelMedium,
    contentPadding: PaddingValues = AppButtonDefaults.contentPadding,
    onClick: VoidCallback
) {

    OutlinedButton(
        modifier = modifier
            .fillMaxWidth()
            .height(62.dp),
        onClick = onClick,
        shape = shape,
        colors = AppButtonColorsDefault.outlinedButtonColor(buttonState = buttonState),
        enabled = buttonState.isEnabled(),
        contentPadding = contentPadding,
        border = AppButtonColorsDefault.borderStoke(
            buttonState = buttonState,
            strokeWidth = strokeWidth
        )
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = text,
            maxLines = 1,
            textAlign = TextAlign.Center,
            style = textStyle,
            color = LocalContentColor.current
        )
    }
}

@Preview
@Composable
private fun PreviewAppButton() {
    MeterAppTheme {
        AppSurface {
            AppOutlinedButton(
                modifier = Modifier,
                buttonState = ButtonState.ENABLED,
                text = "This is outlined button to demonstrate long text"
            ) {

            }
        }
    }
}

fun ButtonState.isEnabled() =
    this != ButtonState.DISABLED