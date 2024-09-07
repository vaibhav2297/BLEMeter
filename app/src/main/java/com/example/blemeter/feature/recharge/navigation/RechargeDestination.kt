package com.example.blemeter.feature.recharge.navigation


import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.blemeter.feature.recharge.presentation.RechargeRoute
import com.example.blemeter.feature.valvecontrol.presentation.ValveControlRoute
import com.example.blemeter.navigation.BLEMeterNavDestination
import com.example.blemeter.utils.VoidCallback

object RechargeDestination : BLEMeterNavDestination {
    override val route: String = "rechargeRoute"
    override val destination: String = "rechargeDestination"
}

fun NavGraphBuilder.rechargeGraph(
    onBackNavigation: VoidCallback
) {
    composable(
        route = RechargeDestination.route
    ) {
        RechargeRoute(
           onBackNavigation = onBackNavigation
        )
    }
}