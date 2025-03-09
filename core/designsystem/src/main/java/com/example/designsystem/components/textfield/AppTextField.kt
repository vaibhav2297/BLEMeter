package com.example.designsystem.components.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.designsystem.components.AppIcon
import com.example.designsystem.icons.AppIcon
import com.example.designsystem.icons.AppIcons
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.theme.MeterAppTheme
import com.example.designsystem.theme.VoidCallback

object AppTextFieldDefault {
    val textFieldHeight = 72.dp
    val maxLines = 1
}

@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    state: BaseTextFieldInputState,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    maxLines: Int = AppTextFieldDefault.maxLines,
    singleLine: Boolean = true,
    textStyle: TextStyle = MaterialTheme.typography.labelMedium,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    trailingIcon: AppIcon? = null,
    onTrailingIconClick: VoidCallback = {}
) {

    OutlinedTextField(
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
                        .clickable(onClick = onTrailingIconClick),
                    icon = trailingIcon,
                    tint = AppTheme.colors.onBackground
                )
            }
        },
        textStyle = textStyle,
        enabled = enabled,
        isError = state.hasError,
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = AppTheme.colors.textHighlighted,
            errorTextColor = AppTheme.colors.error,
            errorBorderColor = AppTheme.colors.error,
            focusedBorderColor = AppTheme.colors.brand,
            unfocusedBorderColor = AppTheme.colors.stroke,
            disabledBorderColor = AppTheme.colors.stroke,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            cursorColor = AppTheme.colors.brand,
            errorCursorColor = AppTheme.colors.error,
            errorSupportingTextColor = AppTheme.colors.error,
            focusedSupportingTextColor = AppTheme.colors.textHighlighted
        ),
        shape = MaterialTheme.shapes.medium
    )
}


@Preview
@Composable
private fun SimulatorTextFieldDarkPreview() {
    MeterAppTheme(
        darkTheme = true
    ) {

        val state = rememberEmailInputState(
            hint = "Email"
        )

        Box(
            modifier = Modifier
                .size(300.dp)
                .background(
                    color = AppTheme.colors.background
                ),
            contentAlignment = Alignment.Center
        ) {
            AppTextField(
                state = state,
                trailingIcon = AppIcon.DrawableResourceIcon(AppIcons.Bluetooth)
            ) {

            }
        }

    }
}
