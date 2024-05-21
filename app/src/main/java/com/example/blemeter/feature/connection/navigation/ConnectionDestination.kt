package com.example.blemeter.feature.connection.navigation


import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.blemeter.feature.connection.presentation.ConnectionRoute
import com.example.blemeter.navigation.BLEMeterNavDestination
import com.example.blemeter.utils.NavigationCallback

object ConnectionDestination : BLEMeterNavDestination {
    override val route: String = "connectionRoute"
    override val destination: String = "connectionDestination"
}

fun NavGraphBuilder.connectionGraph(
    onNavigateToDestination: NavigationCallback,
    nestedGraphs: NavGraphBuilder.() -> Unit
) {
    navigation(
        route = ConnectionDestination.route,
        startDestination = ConnectionDestination.destination,
    ) {
        composable(
            route = ConnectionDestination.destination
        ) {
            ConnectionRoute(
                onNavigateToDestination = onNavigateToDestination
            )
        }
        nestedGraphs()
    }
}