package com.example.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.designsystem.icons.AppIcon
import com.example.designsystem.icons.AppIcons
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.theme.BoxScopeComposable
import com.example.designsystem.theme.MeterAppTheme

object AppTopBarDefaults {
    val HEIGHT = 62.dp
}

@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    title: String,
    leadingContent: BoxScopeComposable = {},
    trailingContent: BoxScopeComposable = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(62.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        //leading
        Box(
            modifier = Modifier
                .size(42.dp),
            content = leadingContent,
            contentAlignment = Alignment.Center
        )

        //Title
        Text(
            modifier = Modifier
                .weight(1f),
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            maxLines = 1,
            textAlign = TextAlign.Center,
            color = AppTheme.colors.onBackground
        )

        //trailing
        Box(
            modifier = Modifier
                .size(42.dp),
            content = trailingContent,
            contentAlignment = Alignment.Center
        )
    }
}

@Preview
@Composable
private fun PreviewAppTopBar() {
    MeterAppTheme {
        AppSurface {
            AppTopBar(
                title = "Wallet",
                leadingContent = {
                    AppIcon(
                        modifier = Modifier.size(24.dp),
                        icon = AppIcon.DrawableResourceIcon(AppIcons.Back),
                        tint = AppTheme.colors.onBackground
                    )
                }
            ) {
                AppIcon(
                    icon = AppIcon.DrawableResourceIcon(AppIcons.Valve),
                    tint = AppTheme.colors.onBackground
                )
            }
        }
    }
}

