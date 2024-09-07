package com.example.blemeter.feature.communication.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.blemeter.R
import com.example.blemeter.core.ble.domain.model.request.AccumulateDataRequest
import com.example.blemeter.core.ble.domain.model.request.PurchaseDataRequest
import com.example.blemeter.core.ble.domain.model.request.ValveInteractionCommand
import com.example.blemeter.core.ble.utils.roundTo
import com.example.blemeter.model.MeterData
import com.example.blemeter.model.ValveControlData
//import com.example.blemeter.ui.components.BLECircularProgressIndicator
//import com.example.blemeter.ui.components.BLEMeterAlertDialog
import com.example.blemeter.ui.theme.MeterAppTheme
import com.example.blemeter.utils.HorizontalSpacer
import com.example.blemeter.utils.ValueChanged
import com.example.blemeter.utils.VerticalSpacer
import com.example.blemeter.utils.VoidCallback
import com.juul.kable.State

@Composable
fun CommunicationRoute(
    navigateBack: VoidCallback,
    viewModel: CommunicationViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CommunicationScreen(
        uiState = uiState,
        navigateBack = navigateBack,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun CommunicationScreen(
    modifier: Modifier = Modifier,
    uiState: CommunicationUiState,
    navigateBack: VoidCallback,
    onEvent: ValueChanged<CommunicationUiEvent>,
) {

    HandleStates(
        uiState = uiState,
        navigateBack = navigateBack
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, true),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            MeterDataSection(
                meterData = uiState.meterData,
                connectionState = uiState.connectionState,
                isReadingMeterData = uiState.isReadingMeterData,
                onEvent = onEvent
            )

            //Valve interaction section
            VerticalSpacer(height = 20.dp)

            ValveInteractionSection(
                valveControlData = uiState.valveControlData,
                onEvent = onEvent
            )

            //Purchase Data section
            VerticalSpacer(height = 20.dp)

            PurchaseDataSection(onEvent = onEvent)

            //Zero Initialise section
            VerticalSpacer(height = 20.dp)

            ZeroInitialiseSection(onEvent = onEvent)

            //Accumulate Data section
            VerticalSpacer(height = 20.dp)

            AccumulateDataSection(onEvent = onEvent)
        }

        Text(
            modifier = modifier.wrapContentSize(),
            text = stringResource(R.string.brand_name)
        )
    }
}

@Composable
fun HandleStates(
    uiState: CommunicationUiState,
    navigateBack: VoidCallback
) {

    when {
        uiState.isLoading -> {
            //BLECircularProgressIndicator()
        }

        !uiState.error.isNullOrEmpty() -> {
           /* BLEMeterAlertDialog(
                title = stringResource(R.string.caution),
                description = uiState.error,
                positiveButtonText = stringResource(R.string.ok),
                onDismiss = { },
                onConfirmation = navigateBack
            )*/
        }

        /*uiState.connectionState.isDisconnected() -> {
            BLEMeterAlertDialog(
                title = stringResource(id = R.string.device_disconnected_title),
                description = stringResource(id = R.string.device_disconnected_desc),
                positiveButtonText = stringResource(R.string.ok),
                onDismiss = { },
                onConfirmation = navigateBack
            )
        }*/
    }
}

@Composable
fun MeterDataSection(
    modifier: Modifier = Modifier,
    meterData: MeterData,
    isReadingMeterData: Boolean,
    connectionState: State,
    onEvent: ValueChanged<CommunicationUiEvent>
) {
    val buttonTxt =
        if (isReadingMeterData) stringResource(id = R.string.stop_read_meter_data) else stringResource(
            id = R.string.read_meter_data
        )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.meter_data),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            maxLines = 1
        )

        VerticalSpacer(height = 6.dp)

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = meterData.totalPurchase.roundTo(2).toString(),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            maxLines = 1
        )

        VerticalSpacer(height = 12.dp)

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onEvent(
                    CommunicationUiEvent.OnMeterDataRead
                )
            },
            //enabled = connectionState.isConnected()
        ) {
            Text(
                text = stringResource(
                    id = R.string.read_meter_data
                ),
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                color = Color.White,
            )
        }
    }
}

@Composable
fun ValveInteractionSection(
    modifier: Modifier = Modifier,
    valveControlData: ValveControlData,
    onEvent: ValueChanged<CommunicationUiEvent>
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Valve State : ${valveControlData.controlState.title()}",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            maxLines = 1
        )

        VerticalSpacer(height = 12.dp)

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    onEvent(
                        CommunicationUiEvent.OnValveInteraction(ValveInteractionCommand.OPEN)
                    )
                },
                //enabled = connectionState.isConnected()
            ) {
                Text(
                    text = stringResource(R.string.valve_on),
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    color = Color.White,
                )
            }
            HorizontalSpacer(width = 4.dp)
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    onEvent(
                        CommunicationUiEvent.OnValveInteraction(ValveInteractionCommand.CLOSE)
                    )
                },
                //enabled = connectionState.isConnected()
            ) {
                Text(
                    text = stringResource(R.string.valve_off),
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    color = Color.White,
                )
            }
        }
    }
}


@Composable
fun PurchaseDataSection(
    modifier: Modifier = Modifier,
    onEvent: ValueChanged<CommunicationUiEvent>
) {
    var purchaseData by remember { mutableStateOf(0u) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.purchase_data),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            maxLines = 1
        )

        VerticalSpacer(height = 12.dp)

        TextField(
            value = purchaseData.toString(),
            onValueChange = { s ->
                purchaseData = if (s.isEmpty()) 0u else s.toUInt()
            },
            suffix = {
                Text(
                    text = stringResource(R.string.meter_cube),
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        )

        VerticalSpacer(height = 8.dp)

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onEvent(
                    CommunicationUiEvent.OnPurchaseData(
                        request = PurchaseDataRequest(
                            purchaseVariable = purchaseData.toDouble(),
                            numberTimes = 1
                        )
                    )
                )
            },
            //enabled = connectionState.isConnected()
        ) {
            Text(
                text = "Write Purchase Data",
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                color = Color.White,
            )
        }
    }
}

@Composable
fun ZeroInitialiseSection(
    modifier: Modifier = Modifier,
    onEvent: ValueChanged<CommunicationUiEvent>
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = {
            onEvent(
                CommunicationUiEvent.OnZeroInitialise
            )
        },
    ) {
        Text(
            text = stringResource(
                id = R.string.zer_initialising
            ),
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            color = Color.White,
        )
    }
}

@Composable
fun AccumulateDataSection(
    modifier: Modifier = Modifier,
    onEvent: ValueChanged<CommunicationUiEvent>
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = {
            onEvent(
                CommunicationUiEvent.OnAccumulateData(
                    request = AccumulateDataRequest(accumulate = 100u)
                )
            )
        },
    ) {
        Text(
            text = stringResource(
                id = R.string.accumulate_data
            ),
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            color = Color.White,
        )
    }
}

@Preview
@Composable
private fun PreviewCommunicationScreen() {
    MeterAppTheme {
        CommunicationScreen(
            modifier = Modifier.background(Color.White),
            uiState = CommunicationUiState(),
            navigateBack = {}
        ) {

        }
    }
}
