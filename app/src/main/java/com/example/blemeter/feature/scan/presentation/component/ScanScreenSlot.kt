package com.example.blemeter.feature.scan.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScanScreenSlot(
    modifier: Modifier = Modifier,
    topContent: @Composable (Modifier) -> Unit,
    centerContent: @Composable (Modifier) -> Unit,
    bottomContent: @Composable (Modifier) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        topContent(
            Modifier
                .align(Alignment.TopCenter)
                .padding(top = 98.dp)
        )

        centerContent(
            Modifier
                .align(Alignment.Center)
        )

        bottomContent(
            Modifier
                .align(Alignment.BottomCenter)
        )
    }
}