package com.example.blemeter.feature.dashboard.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.blemeter.feature.dashboard.domain.model.MeterControl
import com.example.designsystem.components.AppIcon
import com.example.designsystem.components.OutlinedSlot
import com.example.designsystem.components.VerticalSpacer
import com.example.designsystem.icons.AppIcon
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.theme.MeterAppTheme
import com.example.designsystem.theme.ValueChanged
import com.example.designsystem.theme.VoidCallback

@Composable
fun MeterControlIcon(
    modifier: Modifier = Modifier,
    meterControl: MeterControl,
    onClick: ValueChanged<MeterControl>
) {
    Column(
        modifier = modifier
            .width(66.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MeterControlRoundedIcon(
            icon = meterControl.controlIcon,
            onClick = { onClick(meterControl) }
        )

        VerticalSpacer(height = AppTheme.padding.extraSmall)

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = meterControl.displayName)
                .replace(" ", "\n"),
            maxLines = 2,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
            color = AppTheme.colors.textHighlighted
        )
    }
}

@Composable
private fun MeterControlRoundedIcon(
    modifier: Modifier = Modifier,
    icon: AppIcon,
    onClick: VoidCallback
) {
    OutlinedSlot(
        modifier = modifier
            .size(64.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(50),
        strokeWidth = 2.dp
    ) {
        AppIcon(
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.CenterHorizontally),
            icon = icon,
            tint = AppTheme.colors.brand
        )
    }
}

@Preview
@Composable
private fun PreviewMeterControl() {
    MeterAppTheme {
        MeterControlIcon(
            meterControl = MeterControl.RECHARGE
        ) {

        }
    }
}