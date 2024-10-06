package com.example.blemeter.feature.scan.presentation.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.blemeter.R
import com.example.designsystem.components.AppSurface
import com.example.designsystem.components.ButtonState
import com.example.designsystem.components.RoundOutlinedButton
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.theme.MeterAppTheme
import com.example.designsystem.theme.VoidCallback

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
    ) { mod ->
        Text(
            modifier = mod
                .fillMaxWidth(),
            text = stringResource(id = R.string.brand_name),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Normal
            ),
            color = AppTheme.colors.textHighlighted
        )
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