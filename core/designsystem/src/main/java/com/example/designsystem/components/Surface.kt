package com.example.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.theme.MeterAppTheme

object AppSurfaceDefaults {
    val NoCornerShape: Shape
        get() = RoundedCornerShape(0.dp)
}

@Composable
fun AppSurface(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    color: Color = AppTheme.colors.background,
    content: @Composable (padding: PaddingValues) -> Unit
) {
    Surface(
        modifier = modifier,
        color = color,
        shape = shape,
        content = { content(PaddingValues(AppTheme.padding.extraLarge)) }
    )
}

@Preview
@Composable
private fun PreviewAppSurface() {
    MeterAppTheme {
        AppSurface { padding ->
            Box(
                modifier = Modifier.padding(padding)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "This is surface",
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.colors.textPrimary
                )
            }
        }
    }
}
