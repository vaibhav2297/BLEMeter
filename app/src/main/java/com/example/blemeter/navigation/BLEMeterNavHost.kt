package com.example.blemeter.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.blemeter.feature.communication.navigation.communicationGraph
import com.example.blemeter.feature.connection.navigation.ConnectionDestination
import com.example.blemeter.feature.connection.navigation.connectionGraph
import com.example.blemeter.feature.dashboard.navigation.dashboardGraph
import com.example.blemeter.feature.scan.navigation.ScanDestination
import com.example.blemeter.feature.scan.navigation.scanGraph
import com.example.blemeter.feature.setting.navigation.settingGraph
import com.example.blemeter.utils.NavigationCallback
import com.example.blemeter.utils.VoidCallback

@Composable
fun BLEMeterNavHost(
    onBackClick: VoidCallback,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onNavigateToDestination: NavigationCallback,
    startDestination: String = ScanDestination.route,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        connectionGraph(
            onNavigateToDestination = onNavigateToDestination
        ) {
            communicationGraph(
                navigateBack = onBackClick
            )

            settingGraph(
                navigateBack = onBackClick
            )
        }

        scanGraph(
            onNavigateToDestination = onNavigateToDestination
        ) {
            dashboardGraph(
                onNavigateToDestination = onNavigateToDestination
            )
        }
    }
}