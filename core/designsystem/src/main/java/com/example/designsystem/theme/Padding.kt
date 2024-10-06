package com.example.designsystem.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
class AppPadding(
    extraSmall: Dp,
    small: Dp,
    medium: Dp,
    large: Dp,
    extraLarge: Dp
) {

    var extraSmall by mutableStateOf(extraSmall)
        private set
    var small by mutableStateOf(small)
        private set
    var medium by mutableStateOf(medium)
        private set
    var large by mutableStateOf(large)
        private set
    var extraLarge by mutableStateOf(extraLarge)
        private set


    fun update(other: AppPadding) {
        extraSmall = other.extraSmall
        small = other.small

        medium = other.medium
        large = other.large
        extraLarge = other.extraLarge
    }

    fun copy(): AppPadding = AppPadding(
        extraSmall = this.extraSmall,
        small = this.small,
        medium = this.medium,
        large = this.large,
        extraLarge = this.extraLarge
    )
}

val LocalAppPadding = staticCompositionLocalOf<AppPadding> {
    error("No App Padding Provided")
}


object AppPaddingDefaults {

    val phonePadding = AppPadding(
        extraSmall = 6.dp,
        small = 8.dp,
        medium = 12.dp,
        large = 16.dp,
        extraLarge = 24.dp
    )
}