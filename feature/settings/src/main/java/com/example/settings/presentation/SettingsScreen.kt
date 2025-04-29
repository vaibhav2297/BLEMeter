package com.example.settings.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.components.AppAlertDialog
import com.example.designsystem.components.AppIcon
import com.example.designsystem.components.AppScaffold
import com.example.designsystem.components.AppSurface
import com.example.designsystem.components.AppTopBar
import com.example.designsystem.components.HorizontalSpacer
import com.example.designsystem.icons.AppIcon
import com.example.designsystem.icons.AppIcons
import com.example.designsystem.theme.AppStrings
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.theme.MeterAppTheme
import com.example.designsystem.theme.ValueChanged
import com.example.designsystem.theme.VoidCallback
import com.example.designsystem.theme.White

@Composable
internal fun SettingsRoute(
    onNavigateToWallet: VoidCallback,
    onNavigateToAuth: VoidCallback,
    onBack: VoidCallback,
    viewModel: SettingsViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsScreen(
        uiState = uiState,
        onBack = onBack,
        onNavigateToWallet = onNavigateToWallet,
        onNavigateToAuth = onNavigateToAuth,
        onEvent = viewModel::onEvent
    )
}

@Composable
internal fun SettingsScreen(
    modifier: Modifier = Modifier,
    uiState: SettingsState,
    onBack: VoidCallback,
    onNavigateToWallet: VoidCallback,
    onNavigateToAuth: VoidCallback,
    onEvent: ValueChanged<SettingsEvents>
) {
    if (uiState.showLogoutAlert){
        AppAlertDialog(
            title = stringResource(id = AppStrings.logout),
            description = stringResource(id = AppStrings.logoutWarning),
            positiveButtonText = stringResource(AppStrings.logout),
            negativeButtonText = stringResource(AppStrings.cancel),
            onDismiss = {
                onEvent(SettingsEvents.OnLogoutAlert(show = false))
            }
        ) {
            onEvent(SettingsEvents.OnLogout)
        }
    }

    //navigation to wallet
    if (uiState.navigateToWallet) {
        onNavigateToWallet()
        onEvent(SettingsEvents.OnWallet(false))
    }

    //navigation to auth
    if (uiState.navigateToAuth) {
        onNavigateToAuth()
        onEvent(SettingsEvents.OnNavigateToAuth(false))
    }


    AppScaffold(
        modifier = modifier,
        screenState = uiState.screenState,
        topBar = {
            AppTopBar(
                title = "Settings",
                leadingContent = {
                    AppIcon(
                        modifier = Modifier
                            .clickable(onClick = onBack),
                        icon = AppIcon.DrawableResourceIcon(AppIcons.Back),
                        tint = White
                    )
                }
            )
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = AppTheme.padding.extraLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            //wallet
            SettingsItem(
                modifier = Modifier
                    .clickable {
                        onEvent(SettingsEvents.OnWallet(true))
                    },
                icon = AppIcon.DrawableResourceIcon(AppIcons.Wallet),
                title = stringResource(AppStrings.wallet)
            )

            Divider(
                modifier = Modifier,
                thickness = 1.dp,
                color = AppTheme.colors.stroke
            )

            //logout
            SettingsItem(
                modifier = Modifier
                    .clickable {
                        onEvent(SettingsEvents.OnLogoutAlert(show = true))
                    },
                icon = AppIcon.DrawableResourceIcon(AppIcons.Logout),
                title = stringResource(id = AppStrings.logout),
                iconTint = AppTheme.colors.error
            )
        }
    }
}

@Composable
internal fun SettingsItem(
    modifier: Modifier = Modifier,
    icon: AppIcon,
    title: String,
    iconTint: Color = AppTheme.colors.brand
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = AppTheme.padding.large),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppIcon(
            modifier = Modifier
                .size(32.dp),
            icon = icon,
            tint = iconTint
        )

        HorizontalSpacer(width = AppTheme.padding.medium)

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = title,
            maxLines = 1,
            style = MaterialTheme.typography.labelMedium,
            color = AppTheme.colors.textHighlighted
        )
    }
}

@Preview
@Composable
private fun PreviewSettingScreen() {
    MeterAppTheme {
        AppSurface {
            SettingsScreen(
                uiState = SettingsState(),
                onBack = {},
                onNavigateToWallet = {},
                onNavigateToAuth = {}
            ) {

            }
        }
    }
}

@Preview
@Composable
private fun PreviewMeterControl() {
    MeterAppTheme {
        SettingsItem(
            icon = AppIcon.DrawableResourceIcon(AppIcons.Valve),
            title = "Test"
        )
    }
}