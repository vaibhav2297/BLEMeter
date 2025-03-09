package com.example.blemeter.feature.dashboard.navigation


import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.blemeter.feature.dashboard.presentation.DashboardRoute
import com.example.navigation.BLEMeterNavDestination
import com.example.navigation.uitls.NavigationCallback

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