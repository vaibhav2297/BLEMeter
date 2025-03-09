package com.example.blemeter.feature.scan.navigation


import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.blemeter.feature.scan.presentation.ScanRoute
import com.example.navigation.BLEMeterTopLevelDestination
import com.example.navigation.uitls.NavigationCallback

object ScanDestination : BLEMeterTopLevelDestination {
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