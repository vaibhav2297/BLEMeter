package com.example.wallet.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.designsystem.theme.VoidCallback
import com.example.navigation.BLEMeterNavDestination
import com.example.wallet.presentation.rechargewallet.RechargeWalletRoute

object RechargeWalletDestination : BLEMeterNavDestination {
    override val route: String = "rechargeWalletRoute"
    override val destination: String = "rechargeWalletDestination"
}

fun NavGraphBuilder.rechargeWalletGraph(
    onBackNavigate: VoidCallback
) {
    composable(
        route = RechargeWalletDestination.route
    ) {
        RechargeWalletRoute(onBackNavigate = onBackNavigate)
    }
}
