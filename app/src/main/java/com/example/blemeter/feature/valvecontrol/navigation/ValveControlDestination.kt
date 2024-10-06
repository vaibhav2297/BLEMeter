package com.example.blemeter.feature.valvecontrol.navigation


import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.blemeter.feature.valvecontrol.presentation.ValveControlRoute
import com.example.blemeter.config.utils.VoidCallback
import com.example.navigation.BLEMeterNavDestination

object ValveControlDestination : BLEMeterNavDestination {
    override val route: String = "valveControlRoute"
    override val destination: String = "valveControlDestination"
}

fun NavGraphBuilder.valveControlGraph(
    onBackNavigation: VoidCallback
) {
    composable(
        route = ValveControlDestination.route
    ) {
        ValveControlRoute(
           onBackNavigation = onBackNavigation
        )
    }
}