package com.example.blemeter.feature.scan.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.blemeter.R
import com.example.blemeter.ui.components.AppIcon
import com.example.blemeter.ui.components.AppOutlinedButton
import com.example.blemeter.ui.components.AppSurface
import com.example.blemeter.ui.components.ButtonState
import com.example.blemeter.ui.icon.AppIcon
import com.example.blemeter.ui.icon.AppIcons
import com.example.blemeter.ui.theme.AppTheme
import com.example.blemeter.ui.theme.MeterAppTheme
import com.example.blemeter.ui.components.VerticalSpacer
import com.example.blemeter.config.utils.VoidCallback

@Composable
fun NoFoundDeviceSection(
    modifier: Modifier = Modifier,
    onCancel: VoidCallback,
    onReScan: VoidCallback,
) {
    ScanScreenSlot(
        modifier = modifier,
        topContent = { mod ->
            InformationSection(
                modifier = mod,
                title = stringResource(R.string.no_devices_found),
                description = stringResource(R.string.rescan_desc)
            )
        },
        centerContent = { mod ->
            AppIcon(
                modifier = mod
                    .size(250.dp),
                icon = AppIcon.DrawableResourceIcon(AppIcons.NotFound),
                tint = AppTheme.colors.brand
            )
        }
    ) { mod ->
        BottomSection(
            modifier = mod,
            onReScan = onReScan,
            onCancel = onCancel
        )
    }
}

@Preview
@Composable
private fun PreviewNoFoundDeviceSection() {
    MeterAppTheme {
        AppSurface {
            NoFoundDeviceSection(
                onReScan = { },
                onCancel = { }
            )
        }
    }
}

@Composable
private fun BottomSection(
    modifier: Modifier = Modifier,
    onCancel: VoidCallback,
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
            text = stringResource(R.string.cancel),
            buttonState = ButtonState.ENABLED,
            onClick = onCancel
        )
    }
}

