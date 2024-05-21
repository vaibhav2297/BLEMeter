package com.example.blemeter.app

import androidx.compose.runtime.Composable
import com.example.blemeter.navigation.BLEMeterNavHost
import com.example.blemeter.ui.theme.BLEMeterTheme

@Composable
fun BLEMeterApp(
    appState: BLEMeterAppState = rememberBLEMeterAppState()
) {
    BLEMeterTheme {
        BLEMeterNavHost(
            onBackClick = appState::onBackClick,
            navController = appState.navController,
            onNavigateToDestination = appState::navigate
        )
    }
}