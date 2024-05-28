package com.example.blemeter.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BLECircularProgressIndicator(
    modifier: Modifier = Modifier,
    indicatorSize: Dp = 24.dp
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(indicatorSize),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}