package com.example.blemeter.feature.scan.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.blemeter.ui.components.AppSurface
import com.example.blemeter.ui.theme.AppTheme
import com.example.blemeter.ui.theme.MeterAppTheme
import com.example.blemeter.ui.components.VerticalSpacer

@Composable
internal fun InformationSection(
    modifier: Modifier = Modifier,
    title: String,
    description: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = title,
            maxLines = 1,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            color = AppTheme.colors.textPrimary
        )

        VerticalSpacer(height = AppTheme.padding.medium)

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = description,
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
private fun PreviewInformationSection() {
    MeterAppTheme {
        AppSurface {
            InformationSection(
                modifier = Modifier,
                title = "Press Scan to search",
                description = "Make sure meter is nearby and its Bluetooth is turned on"
            )
        }
    }
}