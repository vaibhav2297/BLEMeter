package com.example.blemeter.feature.dashboard.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.example.blemeter.ui.components.AppSurface
import com.example.blemeter.ui.components.OutlinedSlotWithTitle
import com.example.blemeter.ui.components.TitleSlot
import com.example.blemeter.ui.theme.AppTheme
import com.example.blemeter.ui.theme.MeterAppTheme
import com.example.blemeter.utils.TimeUtils
import com.example.blemeter.utils.VerticalSpacer


@Composable
fun OverviewSlot(
    modifier: Modifier = Modifier
) {
    TitleSlot(
        modifier = modifier
            .fillMaxWidth(),
        title = stringResource(R.string.overview)
    ) {
        OverviewSection()
    }
}

@Composable
private fun OverviewSection(
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxWidth(),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
    ) {

        //Accumulated Usage
        item(
            span = { GridItemSpan(maxLineSpan) }
        ) {
            AccumulatedUsageSection(
                usage = 142.3,
                syncTime = System.currentTimeMillis()
            )
        }

        //Total Purchase
        item {
            OutlinedSlotWithTitle(
                title = stringResource(R.string.total_purchase)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = MaterialTheme.typography.labelLarge.fontSize,
                                color = AppTheme.colors.textPrimary
                            )
                        ) {
                            append("25.3")
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
        }

        //Recharge Times
        item {
            OutlinedSlotWithTitle(
                title = stringResource(R.string.recharge_times)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "1",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelLarge,
                    color = AppTheme.colors.textPrimary
                )
            }
        }

        //Valve Status
        item {
            OutlinedSlotWithTitle(
                title = stringResource(R.string.valve_status)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "OPEN",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelLarge,
                    color = AppTheme.colors.textPrimary
                )
            }
        }

        //Battery Voltage
        item {
            OutlinedSlotWithTitle(
                title = stringResource(R.string.battery_voltage)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "NORMAL",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelLarge,
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
            OverviewSlot()
        }
    }
}