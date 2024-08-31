package com.example.blemeter.feature.scan.navigation


import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.blemeter.feature.connection.presentation.ConnectionRoute
import com.example.blemeter.feature.scan.presentation.ScanRoute
import com.example.blemeter.navigation.BLEMeterNavDestination
import com.example.blemeter.utils.NavigationCallback

object ScanDestination : BLEMeterNavDestination {
    override val route: String = "scanRoute"
    override val destination: String = "scanDestination"
}

fun NavGraphBuilder.scanGraph(
    onNavigateToDestination: NavigationCallback,
    nestedGraphs: NavGraphBuilder.() -> Unit
) {
    navigation(
        route = ScanDestination.route,
        startDestination = ScanDestination.destination,
    ) {
        composable(
            route = ScanDestination.destination
        ) {
            ScanRoute(
                onNavigateToDestination = onNavigateToDestination
            )
        }
        nestedGraphs()
    }
}