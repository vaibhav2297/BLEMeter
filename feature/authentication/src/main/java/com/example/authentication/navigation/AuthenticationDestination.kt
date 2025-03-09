package com.example.authentication.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.authentication.presentation.AuthRoute
import com.example.designsystem.theme.VoidCallback
import com.example.navigation.BLEMeterTopLevelDestination
import com.example.navigation.uitls.NavigationCallback

object AuthenticationDestination: BLEMeterTopLevelDestination {
    override val route: String = "authenticationRoute"
    override val destination: String = "authenticationDestination"
}

fun NavGraphBuilder.authenticationGraph(
    onAuthenticated: VoidCallback
) {
    composable(
        route = AuthenticationDestination.route
    ) {
        AuthRoute(onAuthenticated = onAuthenticated)
    }
}
