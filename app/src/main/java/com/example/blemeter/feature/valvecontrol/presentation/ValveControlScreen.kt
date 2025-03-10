package com.example.blemeter.feature.valvecontrol.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.blemeter.R
import com.example.blemeter.config.model.ValveStatus.Companion.toValveInteraction
import com.example.blemeter.core.ble.domain.model.request.ValveInteractionCommand
import com.example.designsystem.components.AppSurface
import com.example.designsystem.components.ButtonState
import com.example.designsystem.components.RoundOutlinedButton
import com.example.designsystem.components.VerticalSpacer
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.theme.MeterAppTheme
import com.example.designsystem.theme.ValueChanged
import com.example.designsystem.theme.VoidCallback

@Composable
fun ValveControlRoute(
    onBackNavigation: VoidCallback,
    viewModel: ValveControlViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ValveControlScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBackNavigation = onBackNavigation
    )
}

@Composable
fun ValveControlScreen(
    modifier: Modifier = Modifier,
    uiState: ValveControlUiState,
    onEvent: ValueChanged<ValveControlUiEvent>,
    onBackNavigation: VoidCallback
) {

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val selectedControl = uiState.valveStatus.toValveInteraction()

        //Title
        Text(
            modifier = Modifier
                .padding(top = 72.dp)
                .fillMaxWidth(),
            text = stringResource(id = R.string.valve_control),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge,
            color = AppTheme.colors.textPrimary
        )

        VerticalSpacer(height = 62.dp)

        //Open Valve
        ValveControlButton(
            command = ValveInteractionCommand.OPEN,
            buttonState = if (selectedControl == ValveInteractionCommand.OPEN)
                ButtonState.ACTIVE
            else ButtonState.ENABLED,
            onClick = {
                onEvent(
                    ValveControlUiEvent.OnValveControl(
                        ValveInteractionCommand.OPEN
                    )
                )
            }
        )

        VerticalSpacer(height = 48.dp)

        //Close Valve
        ValveControlButton(
            command = ValveInteractionCommand.CLOSE,
            buttonState = if (selectedControl == ValveInteractionCommand.CLOSE)
                ButtonState.ACTIVE
            else ButtonState.ENABLED,
            onClick = {
                onEvent(
                    ValveControlUiEvent.OnValveControl(
                        ValveInteractionCommand.CLOSE
                    )
                )
            }
        )
    }
}

@Composable
private fun ValveControlButton(
    modifier: Modifier = Modifier,
    command: ValveInteractionCommand,
    buttonState: ButtonState = ButtonState.ENABLED,
    onClick: ValueChanged<ValveInteractionCommand>
) {
    RoundOutlinedButton(
        modifier = modifier,
        buttonState = buttonState,
        text = command.name,
        onClick = { onClick(command) }
    )
}

@Preview
@Composable
private fun PreviewValveControlScreen() {
    MeterAppTheme {
        AppSurface {
            ValveControlScreen(
                uiState = ValveControlUiState(),
                onEvent = { },
                onBackNavigation = { }
            )
        }
    }
}