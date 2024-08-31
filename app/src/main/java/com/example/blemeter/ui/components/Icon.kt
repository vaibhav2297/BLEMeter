package com.example.blemeter.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.blemeter.R
import com.example.blemeter.ui.icon.AppIcon

object AppIconDefaults {
    val iconSize = 24.dp
}

@Composable
fun AppIcon(
    modifier: Modifier = Modifier,
    icon: AppIcon,
    contentDescription: String = stringResource(R.string.icon),
    tint: Color = LocalContentColor.current
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