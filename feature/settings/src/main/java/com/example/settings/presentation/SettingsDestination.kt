package com.example.wallet.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.designsystem.theme.VoidCallback
import com.example.navigation.BLEMeterNavDestination
import com.example.navigation.uitls.NavigationCallback
import com.example.wallet.presentation.WalletRoute

object WalletDestination : BLEMeterNavDestination {
    override val route: String = "walletRoute"
    override val destination: String = "walletDestination"
}

fun NavGraphBuilder.walletGraph(
    onBackNavigate: VoidCallback,
    onNavigate: NavigationCallback
) {
    composable(
        route = WalletDestination.route
    ) {
        WalletRoute(
            onNavigate = onNavigate,
            onBackNavigate = onBackNavigate
        )
    }
}
