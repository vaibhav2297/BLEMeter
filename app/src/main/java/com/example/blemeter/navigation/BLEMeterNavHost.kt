package com.example.blemeter.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.blemeter.feature.dashboard.navigation.dashboardGraph
import com.example.blemeter.feature.recharge.navigation.rechargeGraph
import com.example.blemeter.feature.scan.navigation.ScanDestination
import com.example.blemeter.feature.scan.navigation.scanGraph
import com.example.blemeter.feature.valvecontrol.navigation.valveControlGraph
import com.example.blemeter.config.utils.NavigationCallback
import com.example.blemeter.config.utils.VoidCallback

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