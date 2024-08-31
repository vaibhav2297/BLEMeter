package com.example.blemeter.feature.setting.navigation


import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.blemeter.feature.communication.navigation.CommunicationDestination
import com.example.blemeter.feature.communication.presentation.CommunicationRoute
import com.example.blemeter.feature.connection.presentation.ConnectionRoute
import com.example.blemeter.feature.setting.presentation.SettingRoute
import com.example.blemeter.navigation.BLEMeterNavDestination
import com.example.blemeter.utils.NavigationCallback
import com.example.blemeter.utils.VoidCallback

object SettingDestination : BLEMeterNavDestination {
    override val route: String = "settingRoute"
    override val destination: String = "settingDestination"
}

fun NavGraphBuilder.settingGraph(
    navigateBack: VoidCallback
) {
    composable(
        route = SettingDestination.route
    ) {
        SettingRoute(navigateBack = navigateBack)
    }
}