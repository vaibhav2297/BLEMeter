package com.example.blemeter.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.blemeter.feature.communication.navigation.communicationGraph
import com.example.blemeter.feature.connection.navigation.ConnectionDestination
import com.example.blemeter.feature.connection.navigation.connectionGraph
import com.example.blemeter.utils.NavigationCallback
import com.example.blemeter.utils.VoidCallback

@Composable
fun BLEMeterNavHost(
    onBackClick: VoidCallback,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onNavigateToDestination: NavigationCallback,
    startDestination: String = ConnectionDestination.route,
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
                onNavigateToDestination = onNavigateToDestination
            )
        }
    }
}