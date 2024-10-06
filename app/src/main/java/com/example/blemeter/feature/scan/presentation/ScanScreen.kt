package com.example.blemeter.feature.scan.presentation

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.blemeter.R
import com.example.blemeter.feature.scan.domain.model.ScanScreenStatus
import com.example.blemeter.feature.scan.presentation.component.DeviceConnectionSection
import com.example.blemeter.feature.scan.presentation.component.FoundDeviceSection
import com.example.blemeter.feature.scan.presentation.component.NoFoundDeviceSection
import com.example.blemeter.feature.scan.presentation.component.ScanInitiateSection
import com.example.blemeter.feature.scan.presentation.component.ScanOngoingSection
import com.example.blemeter.ui.components.AppSurface
import com.example.blemeter.ui.theme.MeterAppTheme
import com.example.blemeter.config.utils.ValueChanged
import com.example.blemeter.core.permission.AppPermissions
import com.example.blemeter.core.permission.RequestAppPermissions
import com.example.blemeter.ui.components.AppAlertDialog
import com.example.navigation.uitls.NavigationCallback

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

    //Permissions
    if (uiState.shouldRequestPermission) {
        RequestBluetoothPermissions { isGranted ->
            onEvent(
                ScanUiEvent.OnPermissionResult(isGranted)
            )
        }
    }

    //device bluetooth
    val bluetoothEnableResult = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        onEvent(
            ScanUiEvent.OnBluetoothEnabled(
                isEnabled = result.resultCode == Activity.RESULT_OK
            )
        )
    }

    if (!uiState.isBluetoothEnabled) {
        AppAlertDialog(
            title = stringResource(id = R.string.bluetooth_is_turned_off),
            description = stringResource(id = R.string.bluetooth_enable_desc),
            positiveButtonText = stringResource(R.string.turn_on),
            negativeButtonText = stringResource(R.string.cancel),
            onDismiss = { }
        ) {
            bluetoothEnableResult.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        }
    }

    when (uiState.screenStatus) {
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
                onCancel = { onEvent(ScanUiEvent.OnConnectionCancel) }
            )
        }

        is ScanScreenStatus.Scanning -> {
            ScanOngoingSection(
                modifier = modifier,
                onCancel = { onEvent(ScanUiEvent.OnScanCancel) }
            )
        }
    }
}

@Composable
private fun RequestBluetoothPermissions(
    onPermission: ValueChanged<Boolean>
) {
    val context = LocalContext.current

    RequestAppPermissions(
        appPermissions = AppPermissions.BluetoothPermissions,
        onPermissionRevoked = {
            onPermission(false)
        },
        navigateToSetting = {
            onPermission(false)
            //Navigate to the setting screen

            context.startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:${context.packageName}")
                )
            )
        }
    ) {
        onPermission(true)
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
                onNavigateToDestination = { destination, route -> }
            ) {

            }
        }
    }
}