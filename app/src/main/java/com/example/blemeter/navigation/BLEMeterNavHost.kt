package com.example.blemeter.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.blemeter.feature.communication.navigation.communicationGraph
import com.example.blemeter.feature.connection.navigation.connectionGraph
import com.example.blemeter.feature.dashboard.navigation.dashboardGraph
import com.example.blemeter.feature.recharge.navigation.rechargeGraph
import com.example.blemeter.feature.scan.navigation.ScanDestination
import com.example.blemeter.feature.scan.navigation.scanGraph
import com.example.blemeter.feature.setting.navigation.settingGraph
import com.example.blemeter.feature.valvecontrol.navigation.valveControlGraph
import com.example.blemeter.utils.NavigationCallback
import com.example.blemeter.utils.VoidCallback

@Composable
fun BLEMeterNavHost(
    onBackNavigation: VoidCallback,
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
                navigateBack = onBackNavigation
            )

            settingGraph(
                navigateBack = onBackNavigation
            )
        }

        scanGraph(
            onNavigateToDestination = onNavigateToDestination
        ) {
            dashboardGraph(
                onNavigateToDestination = onNavigateToDestination
            )

            valveControlGraph(
                onBackNavigation = onBackNavigation
            )

            rechargeGraph(
                onBackNavigation = onBackNavigation
            )
        }
    }
}