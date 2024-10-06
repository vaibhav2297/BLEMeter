package com.example.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.designsystem.icons.AppIcon
import com.example.designsystem.icons.AppIcons
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.theme.MeterAppTheme
import com.example.designsystem.theme.VoidComposable
import com.example.designsystem.theme.White

@Composable
fun TitleSlot(
    modifier: Modifier = Modifier,
    title: String,
    content: VoidComposable
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = title,
            maxLines = 1,
            style = MaterialTheme.typography.titleSmall,
            color = AppTheme.colors.textPrimary
        )

        VerticalSpacer(height = 24.dp)

        content()
    }
}

@Preview
@Composable
private fun PreviewTitleSlot() {
    MeterAppTheme {
        AppSurface {
            TitleSlot(
                title = "Available Devices"
            ) {
                OutlinedSlot {
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
                            tint = White
                        )

                        HorizontalSpacer(width = 14.dp)

                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = "ZYD710512348810",
                            maxLines = 1,
                            style = MaterialTheme.typography.labelSmall,
                            color = AppTheme.colors.textPrimary
                        )
                    }
                }
            }
        }
    }
}