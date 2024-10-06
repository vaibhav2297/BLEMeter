package com.example.blemeter.feature.dashboard.domain.model

import androidx.annotation.StringRes
import com.example.blemeter.R
import com.example.designsystem.icons.AppIcon
import com.example.designsystem.icons.AppIcons

enum class MeterControl(
    val controlIcon: AppIcon,
    @StringRes val displayName: Int
) {
    READ_DATA(
        controlIcon = AppIcon.DrawableResourceIcon(AppIcons.Download),
        displayName = R.string.read_data
    ),

    VALVE_CONTROL(
        controlIcon = AppIcon.DrawableResourceIcon(AppIcons.Valve),
        displayName = R.string.valve_control
    ),

    RECHARGE(
        controlIcon = AppIcon.DrawableResourceIcon(AppIcons.Recharge),
        displayName = R.string.recharge
    ),

    RESET_DATA(
        controlIcon = AppIcon.DrawableResourceIcon(AppIcons.Reset),
        displayName = R.string.reset_data
    ),

    ACCUMULATE(
        controlIcon = AppIcon.DrawableResourceIcon(AppIcons.Accumulate),
        displayName = R.string.accumulate
    )
}