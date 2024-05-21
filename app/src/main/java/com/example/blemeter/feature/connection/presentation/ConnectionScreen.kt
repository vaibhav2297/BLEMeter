package com.example.blemeter.feature.connection.presentation

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.blemeter.R
import com.example.blemeter.core.ble.domain.model.DeviceDetail
import com.example.blemeter.core.ble.domain.model.ScannedDevice
import com.example.blemeter.core.ble.domain.model.isConnected
import com.example.blemeter.core.ble.domain.model.isDisconnected
import com.example.blemeter.feature.communication.navigation.CommunicationDestination
import com.example.blemeter.ui.components.BLEMeterAlertDialog
import com.example.blemeter.ui.theme.BLEMeterTheme
import com.example.blemeter.utils.HorizontalSpacer
import com.example.blemeter.utils.NavigationCallback
import com.example.blemeter.utils.ValueChanged
import com.example.blemeter.utils.VerticalSpacer
import com.example.blemeter.utils.VoidCallback
import com.example.blemeter.utils.permission.AppPermissions
import com.example.blemeter.utils.permission.RequestAppPermissions

@Composable
fun ConnectionRoute(
    onNavigateToDestination: NavigationCallback,
    viewModel: ConnectionViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ConnectionScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToCommunication = { onNavigateToDestination(CommunicationDestination, null) }
    )
}

@Composable
fun ConnectionScreen(
    modifier: Modifier = Modifier,
    uiState: ConnectionUiState,
    onNavigateToCommunication: VoidCallback,
    onEvent: ValueChanged<ConnectionUiEvent>
) {

    ConnectionScreenStateHandle(
        uiState = uiState,
        onEvent = onEvent
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp, 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    onEvent(
                        ConnectionUiEvent.OnStartScan
                    )
                },
                enabled = !uiState.isScanning
            ) {
                Text(
                    text = stringResource(R.string.scan_devices),
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1
                )
            }
            HorizontalSpacer(width = 4.dp)
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    onEvent(
                        ConnectionUiEvent.OnStopScan
                    )
                },
                enabled = uiState.isScanning
            ) {
                Text(
                    text = stringResource(R.string.stop_scan),
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1
                )
            }
        }

        VerticalSpacer(height = 18.dp)

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            if (uiState.scannedDevices.isEmpty() && uiState.isScanning.not()) {
                NoDevicesAvailable(modifier = Modifier.weight(1f))
            } else {
                ScannedDevices(
                    modifier = Modifier.weight(1f),
                    devices = uiState.scannedDevices,
                    connectedDevice = uiState.deviceDetail
                ) { selectedDevice ->
                    onEvent(
                        ConnectionUiEvent.OnConnectionEstablish(selectedDevice)
                    )
                }
            }

            VerticalSpacer(height = 8.dp)

            AnimatedVisibility(visible = uiState.deviceDetail?.connectionState?.isConnected() ?: false) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = onNavigateToCommunication
                ) {
                    Text(
                        text = stringResource(R.string.go_to_communications),
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun ConnectionScreenStateHandle(
    uiState: ConnectionUiState,
    onEvent: ValueChanged<ConnectionUiEvent>
) {
    val context = LocalContext.current


    val bluetoothEnableResult = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        onEvent(
            ConnectionUiEvent.BluetoothEvent.OnBluetoothEnabled
        )
    }

    if (!uiState.isBluetoothEnabled) {
        BLEMeterAlertDialog(
            title = stringResource(id = R.string.bluetooth_is_turned_off),
            description = stringResource(id = R.string.bluetooth_enable_desc),
            positiveButtonText = stringResource(id = R.string.turn_on),
            negativeButtonText = stringResource(R.string.deny),
            onDismiss = {
                onEvent(
                    ConnectionUiEvent.BluetoothEvent.OnBluetoothEnableDismiss
                )
            }
        ) {
            bluetoothEnableResult.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        }
    }

    //Bluetooth Permission
    if (uiState.shouldRequestBluetoothPermission) {
        RequestAppPermissions(
            appPermissions = AppPermissions.BluetoothPermissions,
            onPermissionRevoked = {
                onEvent(
                    ConnectionUiEvent.BluetoothEvent.OnPermissionResult(
                        isGranted = false
                    )
                )
            },
            navigateToSetting = {
                onEvent(
                    ConnectionUiEvent.BluetoothEvent.OnPermissionResult(
                        isGranted = false
                    )
                )
                //Navigate to Setting Screen
                context.startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:${context.packageName}")
                    )
                )

            }
        ) {
            onEvent(
                ConnectionUiEvent.BluetoothEvent.OnPermissionResult(
                    isGranted = true
                )
            )
        }
    }

    //Dialog Handling
    when (uiState.dialog) {
        is ConnectionUiDialog.BluetoothEnableDialog -> {
            BLEMeterAlertDialog(
                title = stringResource(id = R.string.bluetooth_is_turned_off),
                description = stringResource(id = R.string.bluetooth_enable_desc),
                positiveButtonText = stringResource(id = R.string.turn_on),
                negativeButtonText = stringResource(id = R.string.deny),
                onDismiss = {
                    onEvent(
                        ConnectionUiEvent.BluetoothEvent.OnBluetoothEnableDismiss
                    )
                }
            ) {
                bluetoothEnableResult.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            }
        }

        is ConnectionUiDialog.ErrorDialog -> {
            BLEMeterAlertDialog(
                title = stringResource(id = R.string.error_),
                description = uiState.dialog.error,
                onDismiss = {

                },
                onConfirmation = {

                }
            )
        }

        is ConnectionUiDialog.None -> {}
    }
}

@Composable
private fun ScannedDevices(
    modifier: Modifier = Modifier,
    devices: List<ScannedDevice>,
    connectedDevice: DeviceDetail?,
    onDeviceSelect: ValueChanged<ScannedDevice>
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = devices,
            key = { device -> device.address }
        ) { devices ->
            ScannedDevicesItem(
                scannedDevice = devices,
                connectedDevice = connectedDevice
            ) { selectedDevice ->
                onDeviceSelect(selectedDevice)
            }
        }
    }
}

@Composable
private fun ScannedDevicesItem(
    modifier: Modifier = Modifier,
    scannedDevice: ScannedDevice,
    connectedDevice: DeviceDetail?,
    onClick: ValueChanged<ScannedDevice>
) {
    val displayName = scannedDevice.deviceName.ifEmpty { scannedDevice.address }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(scannedDevice) }
            .padding(6.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = displayName,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1
        )

        VerticalSpacer(height = 2.dp)

        connectedDevice?.takeIf {
            it.device?.address == scannedDevice.address
        }?.run {
            if (!connectionState.isDisconnected()) {
                Text(
                    text = connectionState.displayName,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1
                )
            }
        }

        VerticalSpacer(height = 2.dp)

        Divider()
    }
}


@Composable
private fun NoDevicesAvailable(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.no_devices_available),
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun ConnectionScreenPreview() {
    BLEMeterTheme(
        darkTheme = false
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            ConnectionScreen(
                modifier = Modifier,
                uiState = ConnectionUiState(
                    isScanning = false,
                ),
                onNavigateToCommunication = { }
            ) {

            }
        }
    }
}
