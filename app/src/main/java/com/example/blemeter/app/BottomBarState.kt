package com.example.blemeter.app

import androidx.compose.runtime.Stable
import com.example.designsystem.icons.AppIcon
import com.example.navigation.BLEMeterTopLevelDestination

@Stable
class BottomBarState (

) {

}

enum class AppNavItem(
    val destination: BLEMeterTopLevelDestination,
    val icon: AppIcon,
    val label: String
) {

}