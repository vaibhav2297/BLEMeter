package com.example.blemeter.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.blemeter.navigation.BLEMeterNavHost
import com.example.designsystem.components.AppSurface
import com.example.designsystem.components.AppSurfaceDefaults
import com.example.designsystem.theme.MeterAppTheme

@Composable
fun BLEMeterApp(
    appState: BLEMeterAppState = rememberBLEMeterAppState()
) {
    MeterAppTheme {
        AppSurface(
            modifier = Modifier
                .fillMaxSize(),
            shape = AppSurfaceDefaults.NoCornerShape,
        ) { padding ->
            BLEMeterNavHost(
                modifier = Modifier
                    .padding(padding)
                    .systemBarsPadding(),
                onBackNavigation = appState::onBackClick,
                navController = appState.navController,
                onNavigateToDestination = appState::navigate
            )
        }
    }
}