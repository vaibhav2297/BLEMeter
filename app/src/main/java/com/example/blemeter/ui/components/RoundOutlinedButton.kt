package com.example.blemeter.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.blemeter.ui.theme.MeterAppTheme
import com.example.blemeter.config.utils.VoidCallback

object RoundOutlinedButtonDefaults {
    val buttonSize = 200.dp
}

@Composable
fun RoundOutlinedButton(
    modifier: Modifier = Modifier,
    text: String,
    buttonState: ButtonState = ButtonState.ENABLED,
    onClick: VoidCallback
) {
    AppOutlinedButton(
        modifier = modifier
            .size(RoundOutlinedButtonDefaults.buttonSize),
        text = text,
        buttonState = buttonState,
        shape = RoundedCornerShape(50),
        strokeWidth = 2.dp,
        textStyle = MaterialTheme.typography.displaySmall,
        onClick = onClick
    )
}

@Preview
@Composable
private fun PreviewRoundOutlinedButton() {
    MeterAppTheme {
        AppSurface {
            RoundOutlinedButton(
                modifier = Modifier,
                buttonState = ButtonState.ENABLED,
                text = "Connecting"
            ) {

            }
        }
    }
}