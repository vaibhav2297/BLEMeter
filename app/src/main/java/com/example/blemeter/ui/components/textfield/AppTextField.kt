package com.example.blemeter.ui.components.textfield

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.blemeter.ui.icon.AppIcon
import com.example.blemeter.ui.theme.AppTheme
import com.example.blemeter.utils.VoidCallback

object AppTextFieldDefault {
    val textFieldHeight = 72.dp
    val maxLines = 1
}

/*@Composable
fun SimulatorTextField(
    modifier: Modifier = Modifier,
    state: TextFieldInputState = rememberTextFieldInputState(hint = ""),
    enabled: Boolean = true,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    maxLines: Int = AppTextFieldDefault.maxLines,
    singleLine: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.labelMedium,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    trailingIcon: AppIcon? = null,
    onTrailingIconClick: VoidCallback
) {

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .height(AppTextFieldDefault.textFieldHeight),
        value = state.text,
        onValueChange = { state.updateText(it) },
        placeholder = {
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.hint,
                    style = textStyle,
                    color = AppTheme.colors.textHighlighted
                )
            }
        },
        keyboardOptions = keyboardOptions,
        maxLines = maxLines,
        singleLine = singleLine,
        keyboardActions = keyboardActions,
        trailingIcon = {
            if (trailingIcon != null) {
                AppIcon(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraLarge)
                        .clickable { onTrailingIconClick() },
                    icon = trailingIcon,
                    tint = SimulatorTheme.colors.textHighlighted,
                    contentDescription = stringResource(R.string.trail_icon)
                )
            }
        },
        textStyle = textStyle,
        enabled = enabled,
        isError = isError,
        colors = TextFieldDefaults.colors(
            disabledTextColor = AppTheme.colors.textHighlighted,
            errorTextColor = AppTheme.colors.error,
            focusedContainerColor = SimulatorTheme.colors.backgroundLevel2,
            unfocusedContainerColor = SimulatorTheme.colors.backgroundLevel2,
            disabledContainerColor = SimulatorTheme.colors.backgroundLevel2,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = SimulatorTheme.colors.textHighlighted
        ),
        shape = MaterialTheme.shapes.medium
    )
}


@Preview
@Composable
private fun SimulatorTextFieldDarkPreview() {
    HardwareSimulatorTheme(
        darkTheme = true
    ) {
        SimulatorTextField(
            trailingIcon = Icon.ImageVectorIcon(SimulatorIcons.Edit)
        ) {

        }
    }
}*/
