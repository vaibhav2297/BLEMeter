package com.example.blemeter.feature.dashboard.navigation


import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.blemeter.feature.communication.navigation.CommunicationDestination
import com.example.blemeter.feature.communication.presentation.CommunicationRoute
import com.example.blemeter.feature.connection.presentation.ConnectionRoute
import com.example.blemeter.feature.dashboard.presentation.DashboardRoute
import com.example.blemeter.feature.scan.presentation.ScanRoute
import com.example.blemeter.navigation.BLEMeterNavDestination
import com.example.blemeter.utils.NavigationCallback

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