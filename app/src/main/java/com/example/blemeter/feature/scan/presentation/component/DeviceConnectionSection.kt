package com.example.blemeter.feature.scan.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.blemeter.R
import com.example.blemeter.config.extenstions.toDisplay
import com.example.blemeter.ui.components.AppOutlinedButton
import com.example.blemeter.ui.components.AppSurface
import com.example.blemeter.ui.components.ButtonState
import com.example.blemeter.ui.components.RoundOutlinedButton
import com.example.blemeter.ui.theme.AppTheme
import com.example.blemeter.ui.theme.MeterAppTheme
import com.example.blemeter.utils.VoidCallback
import com.juul.kable.State

@Composable
fun DeviceConnectionSection(
    modifier: Modifier = Modifier,
    state: State,
    onCancel: VoidCallback
) {
    ScanScreenSlot(
        modifier = modifier,
        topContent = { mod ->
            InformationSection(
                modifier = mod,
                title = stringResource(R.string.establishing_connection),
                description = stringResource(R.string.the_connection_process_is_underway)
            )
        },
        centerContent = { mod ->
            ConnectionStateView(
                modifier = mod,
                state = state
            )
        }
    ) { mod ->
        AppOutlinedButton(
            modifier = mod,
            text = stringResource(R.string.cancel),
            buttonState = ButtonState.ACTIVE,
            onClick = onCancel
        )
    }
}

@Preview
@Composable
private fun PreviewDeviceConnectionSection() {
    MeterAppTheme {
        AppSurface {
            DeviceConnectionSection(
                state = State.Connected
            ) {

            }
        }
    }
}

@Composable
fun ConnectionStateView(
    modifier: Modifier = Modifier,
    state: State
) {

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        RoundOutlinedButton(
            buttonState = ButtonState.ACTIVE,
            text = state.toDisplay(),
            onClick = { }
        )

        if (state is State.Connecting) {
            CircularProgressIndicator(
                modifier = Modifier.size(200.dp),
                trackColor = AppTheme.colors.brand.copy(alpha = 0.5f)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewConnectionStateView() {
    MeterAppTheme {
        AppSurface {
            ConnectionStateView(
                modifier = Modifier.fillMaxSize(),
                state = State.Connecting.Observes
            )
        }
    }
}