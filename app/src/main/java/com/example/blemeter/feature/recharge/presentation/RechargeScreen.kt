package com.example.blemeter.feature.recharge.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.blemeter.R
import com.example.blemeter.config.extenstions.isDecimal
import com.example.blemeter.feature.scan.presentation.component.ScanScreenSlot
import com.example.designsystem.components.AppOutlinedButton
import com.example.designsystem.components.AppSurface
import com.example.designsystem.components.ButtonState
import com.example.designsystem.components.VerticalSpacer
import com.example.designsystem.components.textfield.TextFieldInputState
import com.example.designsystem.components.textfield.rememberTextFieldInputState
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.theme.MeterAppTheme
import com.example.designsystem.theme.ValueChanged
import com.example.designsystem.theme.VoidCallback
import kotlinx.coroutines.delay

@Composable
fun RechargeRoute(
    onBackNavigation: VoidCallback,
    viewModel: RechargeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RechargeScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBackNavigation = onBackNavigation
    )
}

@Composable
private fun RechargeScreen(
    modifier: Modifier = Modifier,
    uiState: RechargeUiState,
    onBackNavigation: VoidCallback,
    onEvent: ValueChanged<RechargeUiEvent>
) {
    ScanScreenSlot(
        modifier = modifier.fillMaxSize(),
        topContent = { mod ->
            //Title
            Text(
                modifier = mod,
                text = stringResource(id = R.string.recharge),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge,
                color = AppTheme.colors.textPrimary
            )
        },
        centerContent = { mod ->
            AmountSection(
                modifier = mod,
                initialAmount = uiState.rechargeAmount
            ) { amount ->
                if (amount.isDecimal()) {
                    onEvent(
                        RechargeUiEvent.OnRechargeValueChanged(amount.toDouble())
                    )
                }
            }
        }
    ) { mod ->
        AppOutlinedButton(
            modifier = mod,
            text = stringResource(R.string.recharge),
            buttonState = ButtonState.ACTIVE,
            onClick = {
                onEvent(
                    RechargeUiEvent.OnRecharge
                )
            }
        )
    }
}

@Composable
private fun AmountSection(
    modifier: Modifier = Modifier,
    initialAmount: Double,
    onValueChanged: ValueChanged<String>
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val inputState =
            rememberTextFieldInputState(
                hint = "0.0",
                initialText = initialAmount.toString()
            )

        //Desc
        Text(
            text = stringResource(R.string.enter_the_amount),
            style = MaterialTheme.typography.labelMedium,
            color = AppTheme.colors.textHighlighted
        )

        VerticalSpacer(height = AppTheme.padding.extraLarge)

        AmountField(
            state = inputState,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        LaunchedEffect(
            key1 = inputState.text
        ) {
            delay(100)
            onValueChanged(inputState.text.ifEmpty { "0.0" })
        }
    }
}

@Composable
private fun AmountField(
    modifier: Modifier = Modifier,
    state: TextFieldInputState = rememberTextFieldInputState(hint = "0.0"),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    enabled: Boolean = true,
    isError: Boolean = false,
    maxLines: Int = 1,
    singleLine: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.displayLarge,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    TextField(
        modifier = modifier
            .fillMaxWidth(),
        value = state.text,
        onValueChange = { state.updateText(it) },
        placeholder = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.hint,
                    style = textStyle,
                    color = AppTheme.colors.textHighlighted
                )
            }
        },
        textStyle = textStyle.copy(
            textAlign = TextAlign.Center
        ),
        keyboardOptions = keyboardOptions,
        maxLines = maxLines,
        singleLine = singleLine,
        keyboardActions = keyboardActions,
        enabled = enabled,
        isError = isError,
        colors = TextFieldDefaults.colors(
            disabledTextColor = AppTheme.colors.textHighlighted,
            errorTextColor = AppTheme.colors.error,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = AppTheme.colors.textPrimary
        )
    )
}

@Preview
@Composable
private fun PreviewAmountField() {
    MeterAppTheme {
        AppSurface {
            RechargeScreen(
                uiState = RechargeUiState(),
                onEvent = {},
                onBackNavigation = { }
            )
        }
    }
}