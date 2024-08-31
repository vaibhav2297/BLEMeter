package com.example.blemeter.feature.setting.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.blemeter.R
import com.example.blemeter.ui.theme.MeterAppTheme
import com.example.blemeter.utils.HorizontalSpacer
import com.example.blemeter.utils.SpacerFillHorizontally
import com.example.blemeter.utils.ValueChanged
import com.example.blemeter.utils.VoidCallback

@Composable
fun SettingRoute(
    viewModel: SettingViewModel = hiltViewModel(),
    navigateBack: VoidCallback,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun SettingScreen(
    modifier: Modifier = Modifier,
    uiState: SettingUiState,
    onEvent: ValueChanged<SettingUiEvent>
) {

    //Dialog Handling
    when (uiState.dialog) {
        is SettingUiDialog.EditMeterAddressDialog -> {
            ChangeAddressDialog(
                showDialog = true,
                initialAddress = uiState.meterAddress,
                onDismissRequest = {
                    onEvent(
                        SettingUiEvent.OnDialog(SettingUiDialog.None)
                    )
                }
            ) { address ->
                onEvent(
                    SettingUiEvent.OnMeterAddressSave(address)
                )
                onEvent(
                    SettingUiEvent.OnDialog(SettingUiDialog.None)
                )
            }
        }

        is SettingUiDialog.None -> {}
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp, 48.dp)
    ) {
        MeterAddressSection(
            meterAddress = uiState.meterAddress,
            onEvent = onEvent
        )
    }
}

@Composable
fun MeterAddressSection(
    modifier: Modifier = Modifier,
    meterAddress: String,
    onEvent: ValueChanged<SettingUiEvent>
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable {
                onEvent(
                    SettingUiEvent.OnDialog(SettingUiDialog.EditMeterAddressDialog)
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.meter_address),
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1
        )

        SpacerFillHorizontally()

        Text(
            text = meterAddress,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1
        )

        HorizontalSpacer(width = 4.dp)

        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = Icons.Default.Edit,
            contentDescription = ""
        )
    }
}

@Composable
fun ChangeAddressDialog(
    showDialog: Boolean,
    initialAddress: String,
    onDismissRequest: () -> Unit,
    onSaveAddress: (String) -> Unit
) {
    if (showDialog) {
        var address by rememberSaveable { mutableStateOf(initialAddress) }

        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(text = "Change meter address")
            },
            text = {
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Enter new address") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onSaveAddress(address)
                        onDismissRequest()
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismissRequest
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Preview
@Composable
private fun SettingScreenPreview() {
    MeterAppTheme(
        darkTheme = false
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            SettingScreen(
                modifier = Modifier,
                uiState = SettingUiState(
                    meterAddress = "789456123078945",
                ),
            ) {

            }
        }
    }
}