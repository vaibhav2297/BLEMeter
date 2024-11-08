package com.example.designsystem.icons

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.designsystem.R

/**
 * Pay Sync icons. Material icons are [ImageVector]s, custom icons are drawable resource IDs.
 */
object AppIcons {
    // Default Icons
    val Done = Icons.Default.Done

    // Drawable Resources
    val Accumulate = R.drawable.ic_accumulate
    val Back = R.drawable.ic_back_arrow
    val Bluetooth = R.drawable.ic_bluetooth
    val Download = R.drawable.ic_download
    val Recharge = R.drawable.ic_water_recharge
    val Reset = R.drawable.ic_reset
    val Valve = R.drawable.ic_valve
    val NotFound = R.drawable.ic_not_found
    val LoginLogo = R.drawable.ic_login
    val SignUpLogo = R.drawable.ic_signup
    val BackRoundFilled = R.drawable.ic_back_filled_round
    val Wallet = R.drawable.ic_wallet
    val Config = R.drawable.ic_config
}

/**
 * A sealed class to make dealing with [ImageVector] and [DrawableRes] icons easier.
 */
sealed class AppIcon {
    data class ImageVectorIcon(val imageVector: ImageVector) : AppIcon()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : AppIcon()
}

fun getDrawableResource(@DrawableRes id: Int) =
    AppIcon.DrawableResourceIcon(id)

fun getImageVectorIcon(imageVector: ImageVector) =
    AppIcon.ImageVectorIcon(imageVector)
