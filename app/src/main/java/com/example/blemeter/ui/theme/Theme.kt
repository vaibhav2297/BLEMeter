package com.example.blemeter.ui.theme

import android.provider.CalendarContract.Colors
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.example.blemeter.utils.VoidComposable

private val DarkColorScheme = AppColors(
    brand = Primary,
    background = Background,
    onBackground = White,
    textPrimary = White,
    textHighlighted = Grey,
    success = Primary,
    error = Red,
    stroke = Stroke
)

private val LightColorScheme = AppColors(
    brand = Primary,
    background = White,
    onBackground = Background,
    textPrimary = Black,
    textHighlighted = Grey,
    success = Primary,
    error = Red,
    stroke = Stroke
)

@Composable
fun MeterAppTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme
    else LightColorScheme

    val padding = AppPaddingDefaults.phonePadding

    ProvideAppTheme(
        padding = padding,
        colors = colorScheme
    ) {
        MaterialTheme(
            colorScheme = debugColors(),
            typography = AppTypography.PhoneTypography.typography,
            shapes = AppShape.PhoneShape.shapes,
            content = content
        )
    }
}

@Composable
fun ProvideAppTheme(
    colors: AppColors,
    padding: AppPadding,
    content: VoidComposable
) {

    val colorPalette = remember { colors.copy() }
    colorPalette.update(colors)

    val paddingPalettes = remember { padding.copy() }
    paddingPalettes.update(padding)

    CompositionLocalProvider(
        LocalAppPadding provides paddingPalettes,
        LocalAppColors provides colorPalette,
        content = content
    )
}


object AppTheme {

    val colors: AppColors
        @Composable
        get() = LocalAppColors.current

    val padding: AppPadding
        @Composable
        get() = LocalAppPadding.current
}

/**
 * Simulator Custom Color Pallet
 */
@Stable
class AppColors(
    brand: Color,
    background: Color,
    onBackground: Color,
    stroke: Color,
    textPrimary: Color,
    textHighlighted: Color,
    error: Color,
    success: Color
) {

    var brand by mutableStateOf(brand)
        private set
    var background by mutableStateOf(background)
        private set
    var textPrimary by mutableStateOf(textPrimary)
        private set
    var textHighlighted by mutableStateOf(textHighlighted)
        private set
    var error by mutableStateOf(error)
        private set
    var stroke by mutableStateOf(stroke)
        private set
    var success by mutableStateOf(success)
        private set
    var onBackground by mutableStateOf(onBackground)
        private set


    fun update(other: AppColors) {
        brand = other.brand
        background = other.background
        textPrimary = other.textPrimary
        textHighlighted = other.textHighlighted
        error = other.error
        success = other.success
        onBackground = other.onBackground
    }

    fun copy(): AppColors = AppColors(
        brand = this.brand,
        background = this.background,
        textPrimary = this.textPrimary,
        textHighlighted = this.textHighlighted,
        error = this.error,
        success = this.success,
        onBackground = this.onBackground,
        stroke = this.stroke
    )
}

/**
 * Creates a Simulator color LocalComposition
 * */
private val LocalAppColors = staticCompositionLocalOf<AppColors> {
    error("No AppColorPalette provided")
}


/**
 * A Material [Colors] implementation which sets all colors to [debugColor] to discourage usage of
 * [MaterialTheme.colorScheme] in preference to [SimulatorTheme.colors].
 */
fun debugColors(
    debugColor: Color = Color.Black
) = ColorScheme(
    primary = debugColor,
    onPrimary = debugColor,
    primaryContainer = debugColor,
    onPrimaryContainer = debugColor,
    inversePrimary = debugColor,
    secondary = debugColor,
    onSecondary = debugColor,
    secondaryContainer = debugColor,
    onSecondaryContainer = debugColor,
    tertiary = debugColor,
    onTertiary = debugColor,
    tertiaryContainer = debugColor,
    onTertiaryContainer = debugColor,
    background = debugColor,
    onBackground = debugColor,
    surface = debugColor,
    onSurface = debugColor,
    surfaceVariant = debugColor,
    onSurfaceVariant = debugColor,
    surfaceTint = debugColor,
    inverseSurface = debugColor,
    inverseOnSurface = debugColor,
    error = debugColor,
    onError = debugColor,
    errorContainer = debugColor,
    onErrorContainer = debugColor,
    outline = debugColor,
    outlineVariant = debugColor,
    scrim = debugColor
)
