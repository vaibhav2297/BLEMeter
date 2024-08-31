package com.example.blemeter.feature.dashboard.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.blemeter.R
import com.example.blemeter.feature.dashboard.domain.model.MeterControl
import com.example.blemeter.feature.dashboard.presentation.components.MeterControlIcon
import com.example.blemeter.feature.dashboard.presentation.components.OverviewSlot
import com.example.blemeter.ui.components.AppSurface
import com.example.blemeter.ui.components.TitleSlot
import com.example.blemeter.ui.theme.MeterAppTheme
import com.example.blemeter.utils.ValueChanged
import com.example.blemeter.utils.VerticalSpacer

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OverviewSlot()

        VerticalSpacer(height = 48.dp)

        MeterControlSlot { control ->

        }
    }
}

@Preview
@Composable
private fun PreviewDashboardScreen() {
    MeterAppTheme {
        AppSurface {
            DashboardScreen()
        }
    }
}

@Composable
private fun MeterControlSlot(
    modifier: Modifier = Modifier,
    onControl: ValueChanged<MeterControl>
) {

    TitleSlot(
        modifier = modifier
            .fillMaxWidth(),
        title = stringResource(R.string.controls)
    ) {
        MeterControlLazyList(onControl = onControl)
    }
}

@Composable
private fun MeterControlLazyList(
    modifier: Modifier = Modifier,
    onControl: ValueChanged<MeterControl>
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(
            items = MeterControl.entries,
            key = { control -> control.ordinal }
        ) { control ->
            MeterControlIcon(
                meterControl = control,
                onClick = onControl
            )
        }
    }
}

@Preview
@Composable
private fun PreviewMeterControlLazyList() {
    MeterAppTheme {
        AppSurface {
            MeterControlSlot(
                onControl = { }
            )
        }
    }
}