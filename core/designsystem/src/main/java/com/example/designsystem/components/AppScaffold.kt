package com.example.designsystem.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.designsystem.icons.AppIcon
import com.example.designsystem.icons.AppIcons
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.theme.MeterAppTheme
import com.example.designsystem.theme.VoidComposable
import com.example.designsystem.theme.White
import com.example.designsystem.utils.ScreenState

/**
 * A generic composable `AppScaffold` that manages screen state and displays
 * different UI components like the top bar, loading indicator, content, and error messages.
 *
 * @param modifier Modifier to apply to the scaffold layout.
 * @param screenState The current state of the screen which can be one of: Loading, Success, or Error.
 * @param topBar An optional composable that represents the top bar (nullable).
 * @param content The main content of the screen, provided as a composable.
 */
@Composable
fun <T> AppScaffold(
    modifier: Modifier = Modifier,
    screenState: ScreenState<T>,
    topBar: VoidComposable? = null,
    content: VoidComposable
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        ScaffoldContent(
            topBar = topBar,
            content = content
        )

        when (screenState) {
            ScreenState.Loading -> CircularProgressIndicator(
                color = AppTheme.colors.brand
            )

            is ScreenState.Error -> {
                Toast.makeText(context, screenState.error, Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }
}

/**
 * A composable `ScaffoldContent` that displays the main UI content along with an optional top bar.
 *
 * @param modifier Modifier to apply to the scaffold content layout.
 * @param topBar An optional composable that represents the top bar, rendered if non-null.
 * @param content The main content composable that fills the remaining space.
 */
@Composable
private fun ScaffoldContent(
    modifier: Modifier = Modifier,
    topBar: VoidComposable?,
    content: VoidComposable
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //top app bar
        if (topBar != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppTopBarDefaults.HEIGHT),
                contentAlignment = Alignment.Center
            ) {
                topBar()
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun PreviewAppScaffold() {

    val state: ScreenState<String> = ScreenState.None

    MeterAppTheme {
        AppSurface {
            AppScaffold(
                screenState = state,
                topBar = {
                    AppTopBar(title = "title", leadingContent = {
                        AppIcon(
                            icon = AppIcon.DrawableResourceIcon(AppIcons.Back),
                            tint = White
                        )
                    }) {

                    }
                }
            ) {
                Text(
                    text = "This is the content",
                    color = Color.White
                )
            }
        }
    }
}