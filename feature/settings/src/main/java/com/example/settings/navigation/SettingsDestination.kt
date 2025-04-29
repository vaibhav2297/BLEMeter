package com.example.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.designsystem.theme.VoidCallback
import com.example.navigation.BLEMeterNavDestination
import com.example.navigation.uitls.NavigationCallback
import com.example.settings.presentation.SettingsRoute

object SettingsDestination : BLEMeterNavDestination {
    override val route: String = "settingsRoute"
    override val destination: String = "settingsDestination"
}

fun NavGraphBuilder.settingsGraph(
    onBackNavigate: VoidCallback,
    onNavigateToAuth: VoidCallback,
    onNavigateToWallet: VoidCallback
) {
    composable(
        route = SettingsDestination.route
    ) {
        SettingsRoute(
            onNavigateToWallet = onNavigateToWallet,
            onNavigateToAuth = onNavigateToAuth,
            onBack = onBackNavigate
        )
    }
}
