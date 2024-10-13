package com.example.designsystem.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.theme.ValueChanged
import com.example.designsystem.theme.VoidComposable

@Composable
fun <T> AppScaffold(
    modifier: Modifier = Modifier,
    screenState: ScreenState<T>,
    content: VoidComposable
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        content()

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

@Composable
fun <T> AppScaffold(
    modifier: Modifier = Modifier,
    screenState: ScreenState<T>,
    bottomBar: VoidComposable = { },
    navItems: List<AppNavItem>,
    selectedNavItem: AppNavItem,
    onNavItem: ValueChanged<AppNavItem>,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    AppSurface(
        modifier = Modifier
            .fillMaxSize(),
        shape = AppSurfaceDefaults.NoCornerShape
    ) { padding ->

        Column(
            modifier = modifier
                .padding(padding)
                .systemBarsPadding()
                .fillMaxSize()
        ) {

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                content()

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

            /*AppBottomNavBar(
                navItems = navItems,
                selectedItem = selectedNavItem,
                onItemSelect = onNavItem
            )*/
            bottomBar()
        }
    }
}


sealed interface ScreenState<out T> {

    data object Loading : ScreenState<Nothing>

    data class Success<T>(
        val data: T
    ) : ScreenState<T>

    data class Error(val error: String) : ScreenState<Nothing>

    data object None : ScreenState<Nothing>
}

fun <T> ScreenState<T>.isSuccess() = this is ScreenState.Success