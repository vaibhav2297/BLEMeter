package com.example.designsystem.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.icons.AppIcon
import com.example.designsystem.theme.AppTheme

object AppIconDefaults {
    val iconSize = 24.dp
}

@Composable
fun AppIcon(
    modifier: Modifier = Modifier,
    icon: AppIcon,
    contentDescription: String = stringResource(R.string.icon),
    tint: Color = AppTheme.colors.onBackground
) =
    when (icon) {
        is AppIcon.ImageVectorIcon -> {
            Icon(
                modifier = modifier
                    .size(AppIconDefaults.iconSize),
                imageVector = icon.imageVector,
                contentDescription = contentDescription,
                tint = tint
            )
        }

        is AppIcon.DrawableResourceIcon -> {
            Icon(
                modifier = modifier
                    .size(AppIconDefaults.iconSize),
                painter = painterResource(id = icon.id),
                contentDescription = contentDescription,
                tint = tint
            )
        }
    }