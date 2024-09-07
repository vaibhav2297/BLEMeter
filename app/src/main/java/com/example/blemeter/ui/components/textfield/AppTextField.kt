package com.example.blemeter.ui.components.textfield

import androidx.compose.ui.unit.dp

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
