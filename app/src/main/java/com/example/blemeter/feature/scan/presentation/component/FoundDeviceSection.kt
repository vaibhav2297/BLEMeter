package com.example.blemeter.feature.scan.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.blemeter.R
import com.example.designsystem.components.AppIcon
import com.example.designsystem.components.AppOutlinedButton
import com.example.designsystem.components.AppSurface
import com.example.designsystem.components.ButtonState
import com.example.designsystem.components.HorizontalSpacer
import com.example.designsystem.components.OutlinedSlot
import com.example.designsystem.components.VerticalSpacer
import com.example.designsystem.icons.AppIcon
import com.example.designsystem.icons.AppIcons
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.theme.MeterAppTheme
import com.example.designsystem.theme.ValueChanged
import com.example.designsystem.theme.VoidCallback
import com.juul.kable.AndroidAdvertisement

@Composable
fun FoundDeviceSection(
    modifier: Modifier = Modifier,
    foundDevices: List<AndroidAdvertisement>,
    selectedDevice: AndroidAdvertisement?,
    onConnect: VoidCallback,
    onReScan: VoidCallback,
    onDeviceSelect: ValueChanged<AndroidAdvertisement>
) {
    ScanScreenSlot(
        modifier = modifier,
        topContent = { mod ->
            InformationSection(
                modifier = mod,
                title = stringResource(
                    id = R.string.found_devices,
                    foundDevices.size
                ),
                description = stringResource(
                    R.string.select_device_to_connect
                )
            )
        },
        centerContent = { mod ->
            FoundDeviceLazyList(
                modifier = mod,
                foundDevices = foundDevices,
                selectedDevice = selectedDevice,
                onDeviceSelect = onDeviceSelect
            )
        }
    ) { mod ->
        BottomSection(
            modifier = mod,
            selectedDevice = selectedDevice,
            onConnect = onConnect,
            onReScan = onReScan
        )
    }
}

@Preview
@Composable
private fun PreviewFoundDeviceSection() {
    MeterAppTheme {
        AppSurface {
            FoundDeviceSection(
                foundDevices = listOf(),
                selectedDevice = null,
                onConnect = { },
                onReScan = { }
            ) {

            }
        }
    }
}

@Composable
private fun FoundDeviceLazyList(
    modifier: Modifier = Modifier,
    foundDevices: List<AndroidAdvertisement>,
    selectedDevice: AndroidAdvertisement?,
    onDeviceSelect: ValueChanged<AndroidAdvertisement>
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        itemsIndexed(
            items = foundDevices,
            key = { _, device -> device.address  }
        ) { index, device ->
            FoundDeviceItem(
                device = device,
                isSelected = selectedDevice == device,
                onDeviceSelect = onDeviceSelect
            )

            if (index < foundDevices.size - 1) {
                Spacer(modifier = Modifier.height(AppTheme.padding.medium))
            }
        }
    }
}

@Composable
fun FoundDeviceItem(
    modifier: Modifier = Modifier,
    device: AndroidAdvertisement,
    isSelected: Boolean,
    onDeviceSelect: ValueChanged<AndroidAdvertisement>
) {

    val contentColor = if (isSelected) AppTheme.colors.brand
    else AppTheme.colors.textPrimary

    val strokeColor = if (isSelected) AppTheme.colors.brand
    else AppTheme.colors.stroke

    OutlinedSlot(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onDeviceSelect(device)
            },
        strokeColor = strokeColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 19.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppIcon(
                icon = AppIcon.DrawableResourceIcon(AppIcons.Bluetooth),
                tint = contentColor
            )

            HorizontalSpacer(width = 14.dp)

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = device.name ?: device.address,
                maxLines = 1,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor
            )
        }
    }
}

@Preview
@Composable
private fun PreviewFoundDeviceLazyList() {
    MeterAppTheme {
        AppSurface {
            FoundDeviceLazyList(
                foundDevices = listOf(),
                selectedDevice = null
            ) {

            }
        }
    }
}

@Composable
private fun BottomSection(
    modifier: Modifier = Modifier,
    selectedDevice: AndroidAdvertisement?,
    onConnect: VoidCallback,
    onReScan: VoidCallback
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppOutlinedButton(
            text = stringResource(R.string.rescan),
            buttonState = ButtonState.ACTIVE,
            onClick = onReScan
        )

        VerticalSpacer(height = AppTheme.padding.medium)

        AppOutlinedButton(
            text = stringResource(R.string.connect),
            buttonState = if (selectedDevice != null) ButtonState.ACTIVE
            else ButtonState.DISABLED,
            onClick = onConnect
        )
    }
}

@Preview
@Composable
private fun PreviewBottomSection() {
    MeterAppTheme {
        AppSurface {
            BottomSection(
                selectedDevice = null,
                onConnect = { },
                onReScan = { }
            )
        }
    }
}

