package com.example.blemeter.feature.connection.presentation

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.blemeter.R
import com.example.blemeter.config.extenstions.isDisconnected
import com.example.blemeter.feature.communication.navigation.CommunicationDestination
import com.example.blemeter.feature.setting.navigation.SettingDestination
//import com.example.blemeter.ui.components.BLEMeterAlertDialog
import com.example.blemeter.ui.theme.MeterAppTheme
import com.example.blemeter.utils.HorizontalSpacer
import com.example.blemeter.utils.NavigationCallback
import com.example.blemeter.utils.ValueChanged
import com.example.blemeter.utils.VerticalSpacer
import com.example.blemeter.utils.permission.AppPermissions
import com.example.blemeter.utils.permission.RequestAppPermissions
import com.juul.kable.AndroidAdvertisement
import com.juul.kable.State

@Composable
fun ConnectionRoute(
    onNavigateToDestination: NavigationCallback,
    viewModel: ConnViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ConnectionScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToDestination = onNavigateToDestination
    )
}

@Composable
fun ConnectionScreen(
    modifier: Modifier = Modifier,
    uiState: ConnectionUiState,
    onNavigateToDestination: NavigationCallback,
    onEvent: ValueChanged<ConnectionUiEvent>
) {

    ConnectionScreenStateHandle(
        uiState = uiState,
        onEvent = onEvent,
        onNavigateToDestination = onNavigateToDestination
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp, 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //Setting Icon section
        /*Image(
            modifier = Modifier
                .align(Alignment.End)
                .size(24.dp)
                .clickable {
                    onNavigateToDestination(SettingDestination, null)
                },
            imageVector = Icons.Default.Settings,
            contentDescription = ""
        )

        VerticalSpacer(height = 18.dp)*/

        if (uiState.advertisements.isEmpty() && uiState.isScanning.not()) {
            NoDevicesAvailable(modifier = Modifier.weight(1f))
        } else {
            ScannedDevices(
                modifier = Modifier.weight(1f),
                devices = uiState.advertisements,
                state = uiState.state,
                connectedDevice = uiState.connectedDevice
            ) { selectedDevice ->
                onEvent(
                    ConnectionUiEvent.OnConnectionEstablish(selectedDevice)
                )
            }
        }

        VerticalSpacer(height = 18.dp)

        ScanButtons(
            uiState = uiState,
            onEvent = onEvent
        )
    }
}

@Composable
private fun ConnectionScreenStateHandle(
    uiState: ConnectionUiState,
    onNavigateToDestination: NavigationCallback,
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

    if (uiState.isMeterAddressRequired) {
        /*BLEMeterAlertDialog(
            title = stringResource(id = R.string.meter_address_required),
            description = stringResource(id = R.string.meter_address_required_desc),
            positiveButtonText = stringResource(id = R.string.ok),
            negativeButtonText = stringResource(R.string.cancel),
            onDismiss = { }
        ) {
            onNavigateToDestination(SettingDestination, null)
        }*/
    }

    if (uiState.isServiceDiscovered) {
        onEvent(ConnectionUiEvent.OnNavigated)
        onNavigateToDestination(CommunicationDestination, null)
    }

    if (!uiState.isBluetoothEnabled) {
        /*BLEMeterAlertDialog(
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
        }*/
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
            /*BLEMeterAlertDialog(
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
            }*/
        }

        is ConnectionUiDialog.ErrorDialog -> {
            /*BLEMeterAlertDialog(
                title = stringResource(id = R.string.error_),
                description = uiState.dialog.error,
                onDismiss = {

                },
                onConfirmation = {

                }
            )*/
        }

        is ConnectionUiDialog.None -> {}
    }
}

@Composable
fun ScanButtons(
    modifier: Modifier = Modifier,
    uiState: ConnectionUiState,
    onEvent: ValueChanged<ConnectionUiEvent>
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
                maxLines = 1,
                color = Color.White
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
                maxLines = 1,
                color = Color.White
            )
        }
    }
}

@Composable
private fun ScannedDevices(
    modifier: Modifier = Modifier,
    devices: List<AndroidAdvertisement>,
    state: State,
    connectedDevice: AndroidAdvertisement?,
    onDeviceSelect: ValueChanged<AndroidAdvertisement>
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = devices,
            key = { device -> device.address }
        ) { devices ->
            ScannedDevicesItem(
                device = devices,
                state = state,
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
    device: AndroidAdvertisement,
    state: State,
    connectedDevice: AndroidAdvertisement?,
    onClick: ValueChanged<AndroidAdvertisement>
) {
    val displayName = if (!device.name.isNullOrEmpty()) device.name!! else device.address

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(device) }
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
            it.address == device.address
        }?.run {
            if (!state.isDisconnected()) {
                Text(
                    text = state.toString(),
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

@Composable
private fun MeterAddressRequiredSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.meter_address_required),
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis
        )

        VerticalSpacer(height = 12.dp)

        Text(
            text = stringResource(R.string.meter_address_required_desc),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun ConnectionScreenPreview() {
    MeterAppTheme(
        darkTheme = false
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            ConnectionScreen(
                modifier = Modifier,
                uiState = ConnectionUiState(
                    isScanning = false,
                ),
                onNavigateToDestination = { destination, route ->

                }
            ) {

            }
        }
    }
}
