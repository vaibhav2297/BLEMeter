package com.example.blemeter.feature.scan.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.blemeter.feature.scan.domain.model.ScanScreenStatus
import com.example.blemeter.feature.scan.presentation.component.DeviceConnectionSection
import com.example.blemeter.feature.scan.presentation.component.FoundDeviceSection
import com.example.blemeter.feature.scan.presentation.component.NoFoundDeviceSection
import com.example.blemeter.feature.scan.presentation.component.ScanInitiateSection
import com.example.blemeter.feature.scan.presentation.component.ScanOngoingSection
import com.example.blemeter.ui.components.AppSurface
import com.example.blemeter.ui.theme.MeterAppTheme
import com.example.blemeter.utils.NavigationCallback
import com.example.blemeter.utils.ValueChanged

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

    //navigation
    if (uiState.navigateTo != null) {
        onNavigateToDestination(uiState.navigateTo, null)
        onEvent(ScanUiEvent.OnNavigated)
    }

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