package com.example.wallet.presentation.rechargewallet

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.components.AppOutlinedButton
import com.example.designsystem.components.ButtonState
import com.example.designsystem.components.VerticalScreenSlot
import com.example.designsystem.components.VerticalSpacer
import com.example.designsystem.components.textfield.TextFieldInputState
import com.example.designsystem.components.textfield.rememberTextFieldInputState
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.theme.ValueChanged
import com.example.designsystem.theme.VoidCallback
import com.example.payment.PaymentActivity
import com.example.wallet.R
import kotlinx.coroutines.delay

@Composable
internal fun RechargeWalletRoute(
    viewModel: RechargeWalletViewModel = hiltViewModel(),
    onBackNavigate: VoidCallback
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RechargeWalletScreen(
        uiState = uiState,
        onEvents = viewModel::onEvent,
        onCancel = onBackNavigate
    )
}

@Composable
private fun RechargeWalletScreen(
    modifier: Modifier = Modifier,
    uiState: RechargeWalletUiState,
    onEvents: ValueChanged<RechargeWalletEvents>,
    onCancel: VoidCallback
) {

    //Register activity
    val paymentResult = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d("ActivityResult", "RechargeWalletScreen: ${result.resultCode}  ${result.data}")
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { d ->
                onEvents(
                    RechargeWalletEvents.OnPaymentResult(d)
                )
            }

            //navigating back
            onCancel()
        }
    }

    //onNavigate to payment
    if (uiState.navigateToPayment) {
        val context = LocalContext.current

        val intent = Intent(context, PaymentActivity::class.java).apply {
            putExtra(PaymentActivity.PAYMENT_OPTION, uiState.paymentOptions)
        }
        paymentResult.launch(intent)

        onEvents(RechargeWalletEvents.OnNavigatedToPayment)
    }


    VerticalScreenSlot(
        modifier = modifier.fillMaxSize(),
        topContent = { mod ->
            //Title
            Text(
                modifier = mod,
                text = stringResource(R.string.wallet_recharge),
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
                    onEvents(
                        RechargeWalletEvents.OnRechargeAmountChange(amount.toDouble())
                    )
                }
            }
        }
    ) { mod ->
        ButtonSection(
            modifier = mod,
            onRecharge = {
                onEvents(
                    RechargeWalletEvents.OnRecharge
                )
            },
            onCancel = onCancel
        )
    }
}

@Composable
private fun AmountSection(
    modifier: Modifier = Modifier,
    initialAmount: Double,
    onAmountChange: ValueChanged<String>
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
            onAmountChange(inputState.text.ifEmpty { "0.0" })
        }
    }
}

//TODO:: Replace with the appTextField
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

@Composable
fun ButtonSection(
    modifier: Modifier = Modifier,
    onRecharge: VoidCallback,
    onCancel: VoidCallback,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppOutlinedButton(
            text = stringResource(R.string.recharge),
            buttonState = ButtonState.ACTIVE,
            onClick = onRecharge
        )

        VerticalSpacer()

        AppOutlinedButton(
            text = stringResource(R.string.cancel),
            buttonState = ButtonState.ENABLED,
            onClick = onCancel
        )
    }
}

private fun String.isDecimal() =
    this.matches("\\d+(?:\\.\\d+)?".toRegex())