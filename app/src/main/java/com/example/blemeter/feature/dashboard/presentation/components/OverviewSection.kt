package com.example.blemeter.feature.dashboard.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.blemeter.R
import com.example.blemeter.feature.dashboard.presentation.DashboardUiState
import com.example.blemeter.config.utils.TimeUtils
import com.example.designsystem.components.AppSurface
import com.example.designsystem.components.OutlinedSlotWithTitle
import com.example.designsystem.components.TitleSlot
import com.example.designsystem.components.VerticalSpacer
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.theme.MeterAppTheme


@Composable
fun OverviewSlot(
    modifier: Modifier = Modifier,
    uiState: DashboardUiState
) {
    TitleSlot(
        modifier = modifier
            .fillMaxWidth(),
        title = stringResource(R.string.overview)
    ) {
        OverviewSection(uiState = uiState)
    }
}

@Composable
private fun OverviewSection(
    modifier: Modifier = Modifier,
    uiState: DashboardUiState
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppTheme.padding.large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //Accumulated Usage
        AccumulatedUsageSection(
            usage = uiState.meterData.accumulatedUsage,
            syncTime = uiState.lastSync
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.padding.large),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //Total Purchase
            OutlinedSlotWithTitle(
                modifier = Modifier
                    .weight(1f),
                title = stringResource(R.string.total_purchase)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                color = AppTheme.colors.textPrimary
                            )
                        ) {
                            append("${uiState.meterData.totalPurchase}")
                        }
                        append(" ")

                        withStyle(
                            style = SpanStyle(
                                fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                color = AppTheme.colors.textHighlighted,
                            )
                        ) {
                            append("m")
                        }

                        withStyle(
                            style = SpanStyle(
                                fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                color = AppTheme.colors.textHighlighted,
                                baselineShift = BaselineShift.Superscript
                            )
                        ) {
                            append("3")
                        }
                    },
                    textAlign = TextAlign.Center,
                )
            }

            //Recharge Times
            OutlinedSlotWithTitle(
                modifier = Modifier
                    .weight(1f),
                title = stringResource(R.string.recharge_times)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = uiState.meterData.numberTimes.toString(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    color = AppTheme.colors.textPrimary
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.padding.large),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //Valve Status
            OutlinedSlotWithTitle(
                modifier = Modifier
                    .weight(1f),
                title = stringResource(R.string.valve_status)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = uiState.meterData.statuses.controlState.title(),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium,
                    color = AppTheme.colors.textPrimary
                )
            }

            //Battery Voltage
            OutlinedSlotWithTitle(
                modifier = Modifier
                    .weight(1f),
                title = stringResource(R.string.battery_voltage)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = uiState.meterData.statuses.batteryState.name,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium,
                    color = AppTheme.colors.textPrimary
                )
            }
        }
    }
}

@Composable
private fun AccumulatedUsageSection(
    modifier: Modifier = Modifier,
    usage: Double,
    syncTime: Long
) {
    OutlinedSlotWithTitle(
        modifier = modifier,
        title = stringResource(R.string.accumulated_usage)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontFamily = MaterialTheme.typography.headlineMedium.fontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                            color = AppTheme.colors.brand
                        )
                    ) {
                        append("$usage")
                    }
                    append(" ")

                    withStyle(
                        style = SpanStyle(
                            fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            color = AppTheme.colors.textHighlighted,
                        )
                    ) {
                        append("m")
                    }

                    withStyle(
                        style = SpanStyle(
                            fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            color = AppTheme.colors.textHighlighted,
                            baselineShift = BaselineShift.Superscript
                        )
                    ) {
                        append("3")
                    }

                },
                textAlign = TextAlign.Center
            )

            VerticalSpacer(height = AppTheme.padding.extraLarge)

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(R.string.last_sync, TimeUtils.formatDate(syncTime)),
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.colors.textHighlighted
            )
        }
    }
}

@Preview
@Composable
private fun PreviewOverviewSection() {
    MeterAppTheme {
        AppSurface {
            OverviewSlot(
                uiState = DashboardUiState()
            )
        }
    }
}