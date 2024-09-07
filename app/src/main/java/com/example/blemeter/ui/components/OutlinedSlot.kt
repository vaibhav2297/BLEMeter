package com.example.blemeter.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.blemeter.ui.theme.AppShape
import com.example.blemeter.ui.theme.AppTheme
import com.example.blemeter.ui.theme.MeterAppTheme
import com.example.blemeter.config.utils.ColumnScopeComposable
import com.example.blemeter.config.utils.VoidComposable

object OutlinedSlotDefaults {
    val strokeWidth = 1.dp
}

@Composable
fun OutlinedSlotWithTitle(
    modifier: Modifier = Modifier,
    title: String,
    shape: Shape = AppShape.PhoneShape.shapes.medium,
    strokeWidth: Dp = OutlinedSlotDefaults.strokeWidth,
    content: VoidComposable
) {
    OutlinedSlot(
        modifier = modifier,
        shape = shape,
        strokeWidth = strokeWidth
    ) {
        Column(
            modifier = Modifier
                .padding(
                    vertical = AppTheme.padding.extraLarge,
                    horizontal = AppTheme.padding.medium
                )
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = title,
                maxLines = 1,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
                color = AppTheme.colors.textHighlighted
            )

            VerticalSpacer(height = 22.dp)

            content()
        }
    }
}

@Composable
fun OutlinedSlot(
    modifier: Modifier = Modifier,
    shape: Shape = AppShape.PhoneShape.shapes.medium,
    strokeWidth: Dp = OutlinedSlotDefaults.strokeWidth,
    strokeColor: Color = AppTheme.colors.stroke,
    content: ColumnScopeComposable
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = strokeWidth,
                color = strokeColor,
                shape = shape
            ),
        verticalArrangement = Arrangement.Center,
        content = content
    )
}

@Preview
@Composable
private fun PreviewRoundedOutlinedSlot() {
    MeterAppTheme {
        OutlinedSlot(
            modifier = Modifier
                .size(200.dp),
            shape = RoundedCornerShape(50),
            strokeWidth = 2.dp
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Scan",
                maxLines = 1,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displaySmall,
                color = AppTheme.colors.textPrimary
            )
        }
    }
}

@Preview
@Composable
private fun PreviewOutlinedSlot() {
    MeterAppTheme {
        OutlinedSlot(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
        ) {
            Text(text = "This is an outlined slot")
        }
    }
}

@Preview
@Composable
private fun PreviewOutlinedSlotWithTitle() {
    MeterAppTheme {
        AppSurface {
            OutlinedSlotWithTitle(
                modifier = Modifier
                    .fillMaxWidth(),
                title = "Title"
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Content will go here",
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.colors.textPrimary
                )
            }
        }
    }
}