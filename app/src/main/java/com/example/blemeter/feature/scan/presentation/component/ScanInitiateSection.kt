package com.example.blemeter.feature.scan.presentation.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.blemeter.R
import com.example.blemeter.ui.components.AppSurface
import com.example.blemeter.ui.components.ButtonState
import com.example.blemeter.ui.components.RoundOutlinedButton
import com.example.blemeter.ui.theme.MeterAppTheme
import com.example.blemeter.utils.VoidCallback

@Composable
fun ScanInitiateSection(
    modifier: Modifier = Modifier,
    onScan: VoidCallback
) {
    ScanScreenSlot(
        modifier = modifier
            .fillMaxSize(),
        topContent = { mod ->
            InformationSection(
                modifier = mod,
                title = stringResource(
                    id = R.string.press_scan_to_start_search
                ),
                description = stringResource(
                    R.string.meter_is_nearby_desc
                )
            )
        },
        centerContent = { mod ->
            RoundOutlinedButton(
                modifier = mod,
                buttonState = ButtonState.ENABLED,
                text = stringResource(id = R.string.scan),
                onClick = onScan
            )
        }
    ) {
    }
}

@Preview
@Composable
private fun PreviewScanInitiateSection() {
    MeterAppTheme {
        AppSurface {
            ScanInitiateSection(
                onScan = { }
            )
        }
    }
}