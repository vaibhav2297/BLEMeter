package com.example.blemeter.feature.scan.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.blemeter.R
import com.example.blemeter.feature.connection.navigation.ConnectionDestination
import com.example.blemeter.feature.connection.presentation.ConnViewModel
import com.example.blemeter.feature.connection.presentation.ConnectionScreen
import com.example.blemeter.feature.scan.domain.model.ScanScreenStatus
import com.example.blemeter.feature.scan.presentation.component.DeviceConnectionSection
import com.example.blemeter.feature.scan.presentation.component.FoundDeviceSection
import com.example.blemeter.feature.scan.presentation.component.InformationSection
import com.example.blemeter.feature.scan.presentation.component.NoFoundDeviceSection
import com.example.blemeter.feature.scan.presentation.component.ScanInitiateSection
import com.example.blemeter.feature.scan.presentation.component.ScanOngoingSection
import com.example.blemeter.ui.components.AppOutlinedButton
import com.example.blemeter.ui.components.AppSurface
import com.example.blemeter.ui.components.ButtonState
import com.example.blemeter.ui.components.RoundOutlinedButton
import com.example.blemeter.ui.theme.AppTheme
import com.example.blemeter.ui.theme.MeterAppTheme
import com.example.blemeter.utils.NavigationCallback
import com.example.blemeter.utils.ValueChanged
import com.example.blemeter.utils.VerticalSpacer
import com.example.blemeter.utils.VoidCallback
import com.example.blemeter.utils.VoidComposable

@Composable
fun ScanRoute(
    onNavigateToDestination: NavigationCallback,
    viewModel: ScanViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ScanScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToDestination = onNavigateToDestination
    )
}

@Composable
private fun ScanScreen(
    modifier: Modifier = Modifier,
    uiState: ScanUiState,
    onNavigateToDestination: NavigationCallback,
    onEvent: ValueChanged<ScanUiEvent>
) {

    when(uiState.screenStatus) {
        is ScanScreenStatus.None -> {
            ScanInitiateSection(
                modifier = modifier,
                onScan = { onEvent(ScanUiEvent.OnScan) }
            )
        }
        is ScanScreenStatus.OnDevicesFound -> {
            FoundDeviceSection(
                foundDevices = uiState.screenStatus.foundDevices,
                selectedDevice = uiState.selectedDevice,
                onConnect = {
                    onEvent(ScanUiEvent.OnConnect)
                },
                onReScan = {
                    onEvent(ScanUiEvent.OnScan)
                },
                onDeviceSelect = { device ->
                    onEvent(ScanUiEvent.OnDeviceSelect(device))
                }
            )
        }
        is ScanScreenStatus.NoDeviceFound -> {
            NoFoundDeviceSection(
                modifier = modifier,
                onCancel = { onEvent(ScanUiEvent.OnScanCancel) },
                onReScan = { onEvent(ScanUiEvent.OnScan) }
            )
        }
        is ScanScreenStatus.OnError -> {

        }
        is ScanScreenStatus.OnConnection -> {
            DeviceConnectionSection(
                modifier = modifier,
                state = uiState.screenStatus.state,
                onCancel =  { onEvent(ScanUiEvent.OnConnectionCancel) }
            )
        }
        is ScanScreenStatus.Scanning -> {
            ScanOngoingSection(
                modifier = modifier,
                onCancel =  { onEvent(ScanUiEvent.OnScanCancel) }
            )
        }
    }
}


@Preview
@Composable
private fun PreviewDeviceScanScreen() {
    MeterAppTheme {
        AppSurface {
            ScanScreen(
                uiState = ScanUiState(
                    isScanning = true
                ),
                onNavigateToDestination = { destination, route ->  }
            ) {

            }
        }
    }
}