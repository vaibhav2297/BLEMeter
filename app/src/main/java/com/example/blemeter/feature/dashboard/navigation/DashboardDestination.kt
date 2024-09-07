package com.example.blemeter.feature.dashboard.navigation


import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.blemeter.feature.dashboard.presentation.DashboardRoute
import com.example.blemeter.navigation.BLEMeterNavDestination
import com.example.blemeter.config.utils.NavigationCallback

object DashboardDestination : BLEMeterNavDestination {
    override val route: String = "dashboardRoute"
    override val destination: String = "dashboardDestination"
}

fun NavGraphBuilder.dashboardGraph(
    onNavigateToDestination: NavigationCallback
) {
    composable(
        route = DashboardDestination.route
    ) {
        DashboardRoute(
            onNavigateToDestination = onNavigateToDestination
        )
    }
}