package com.example.blemeter.feature.communication.navigation


import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.blemeter.feature.communication.presentation.CommunicationRoute
import com.example.blemeter.navigation.BLEMeterNavDestination
import com.example.blemeter.utils.NavigationCallback
import com.example.blemeter.utils.VoidCallback

object CommunicationDestination : BLEMeterNavDestination {
    override val route: String = "CommunicationRoute"
    override val destination: String = "communicationDestination"
}

fun NavGraphBuilder.communicationGraph(
    navigateBack: VoidCallback
) {
    composable(
        route = CommunicationDestination.route
    ) {
        CommunicationRoute(
            navigateBack = navigateBack
        )
    }
}