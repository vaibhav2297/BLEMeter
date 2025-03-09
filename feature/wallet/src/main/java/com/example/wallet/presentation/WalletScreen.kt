package com.example.wallet.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.components.AppIcon
import com.example.designsystem.components.AppOutlinedButton
import com.example.designsystem.components.AppScaffold
import com.example.designsystem.components.AppTopBar
import com.example.designsystem.components.ButtonState
import com.example.designsystem.components.VerticalSpacer
import com.example.designsystem.icons.AppIcons
import com.example.designsystem.icons.getDrawableResource
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.theme.VoidCallback
import com.example.navigation.uitls.NavigationCallback
import com.example.wallet.R
import com.example.wallet.navigation.RechargeWalletDestination

@Composable
internal fun WalletRoute(
    viewModel: WalletViewModel = hiltViewModel(),
    onNavigate: NavigationCallback,
    onBackNavigate: VoidCallback
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WalletScreen(
        uiState = uiState,
        onAddMoney = { onNavigate(RechargeWalletDestination, null) },
        onBackNavigate = onBackNavigate
    )
}

@Composable
private fun WalletScreen(
    uiState: WalletUiState,
    modifier: Modifier = Modifier,
    onAddMoney: VoidCallback,
    onBackNavigate: VoidCallback
) {

    AppScaffold(
        modifier = modifier,
        screenState = uiState.state,
        topBar = {
            AppTopBar(
                title = stringResource(R.string.wallet),
                leadingContent = {
                    AppIcon(
                        modifier = Modifier
                            .clickable(onClick = onBackNavigate),
                        icon = getDrawableResource(AppIcons.Back)
                    )
                }
            )
        }
    ) {
        WalletSection(
            amount = uiState.amount,
            onAddMoney = onAddMoney
        )
    }
}

@Composable
private fun WalletSection(
    modifier: Modifier = Modifier,
    amount: Double,
    onAddMoney: VoidCallback
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "₹ $amount",
            style = MaterialTheme.typography.displayMedium,
            color = AppTheme.colors.onBackground
        )

        VerticalSpacer(height = 48.dp)

        AppOutlinedButton(
            text = stringResource(R.string.add_money),
            buttonState = ButtonState.ACTIVE,
            onClick = onAddMoney
        )
    }
}

@Composable
private fun TransactionSection(
    modifier: Modifier = Modifier
) {

}