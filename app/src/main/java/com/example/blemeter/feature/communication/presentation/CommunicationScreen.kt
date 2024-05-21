package com.example.blemeter.feature.communication.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.blemeter.R
import com.example.blemeter.core.ble.domain.model.ConnectionState
import com.example.blemeter.core.ble.domain.model.isConnected
import com.example.blemeter.core.ble.domain.model.request.ValveControlCommandStatus
import com.example.blemeter.feature.communication.navigation.CommunicationDestination
import com.example.blemeter.model.MeterData
import com.example.blemeter.model.ValveControlData
import com.example.blemeter.utils.HorizontalSpacer
import com.example.blemeter.utils.NavigationCallback
import com.example.blemeter.utils.ValueChanged
import com.example.blemeter.utils.VerticalSpacer

@Composable
fun CommunicationRoute(
    onNavigateToDestination: NavigationCallback,
    viewModel: CommunicationViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CommunicationScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToCommunication = { onNavigateToDestination(CommunicationDestination, null) }
    )
}

@Composable
fun CommunicationScreen(
    modifier: Modifier = Modifier,
    uiState: CommunicationUiState,
    onNavigateToCommunication: () -> Unit,
    onEvent: ValueChanged<CommunicationUiEvent>,
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp, 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (uiState.isCommunicating) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }

        MeterDataSection(
            meterData = uiState.meterData,
            connectionState = uiState.connectionState,
            onEvent = onEvent
        )

        VerticalSpacer(height = 20.dp)

        ValveInteractionSection(
            valveControlData = uiState.valveControlData,
            connectionState = uiState.connectionState
        ) {

        }
    }
}

@Composable
fun MeterDataSection(
    modifier: Modifier = Modifier,
    meterData: MeterData,
    connectionState: ConnectionState,
    onEvent: ValueChanged<CommunicationUiEvent>
) {
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
            text = meterData.accumulatedUsage.toString(),
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
            enabled = connectionState.isConnected()
        ) {
            Text(
                text = stringResource(R.string.read_meter_data),
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1
            )
        }
    }
}

@Composable
fun ValveInteractionSection(
    modifier: Modifier = Modifier,
    valveControlData: ValveControlData,
    connectionState: ConnectionState,
    onEvent: ValueChanged<CommunicationUiEvent>
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Button(
            modifier = Modifier.weight(1f),
            onClick = {
                onEvent(
                    CommunicationUiEvent.OnValveInteraction(ValveControlCommandStatus.OPEN)
                )
            },
            enabled = connectionState.isConnected()
        ) {
            Text(
                text = stringResource(R.string.valve_on),
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1
            )
        }
        HorizontalSpacer(width = 4.dp)
        Button(
            modifier = Modifier.weight(1f),
            onClick = {
                onEvent(
                    CommunicationUiEvent.OnValveInteraction(ValveControlCommandStatus.CLOSE)
                )
            },
            enabled = connectionState.isConnected()
        ) {
            Text(
                text = stringResource(R.string.valve_off),
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1
            )
        }
    }
}
