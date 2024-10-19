package com.example.blemeter.feature.dashboard.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.withResumed
import com.example.blemeter.R
import com.example.blemeter.feature.dashboard.domain.model.MeterControl
import com.example.blemeter.feature.dashboard.presentation.components.MeterControlIcon
import com.example.blemeter.feature.dashboard.presentation.components.OverviewSlot
import com.example.designsystem.components.AppIcon
import com.example.designsystem.components.AppScaffold
import com.example.designsystem.components.AppSurface
import com.example.designsystem.components.AppTopBar
import com.example.designsystem.components.TitleSlot
import com.example.designsystem.components.VerticalSpacer
import com.example.designsystem.icons.AppIcons
import com.example.designsystem.icons.getDrawableResource
import com.example.designsystem.theme.MeterAppTheme
import com.example.designsystem.theme.ValueChanged
import com.example.navigation.uitls.NavigationCallback
import com.example.wallet.navigation.WalletDestination

@Composable
fun DashboardRoute(
    onNavigateToDestination: NavigationCallback,
    viewModel: DashboardViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DashboardScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToDestination = onNavigateToDestination
    )
}

@Composable
private fun DashboardScreen(
    modifier: Modifier = Modifier,
    uiState: DashboardUiState,
    onNavigateToDestination: NavigationCallback,
    onEvent: ValueChanged<DashboardUiEvent>
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                onEvent(
                    DashboardUiEvent.OnRefresh
                )
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    AppScaffold(
        modifier = modifier,
        screenState = uiState.screenState,
        topBar = {
            AppTopBar(
                title = stringResource(R.string.dashboard),
                trailingContent = {
                    AppIcon(
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {
                                onNavigateToDestination(WalletDestination, null)
                            },
                        icon = getDrawableResource(AppIcons.Wallet)
                    )
                }
            )
        }
    ) {
        DashboardContent(
            uiState = uiState,
            onNavigateToDestination = onNavigateToDestination,
            onEvent = onEvent
        )
    }
}

@Composable
private fun DashboardContent(
    modifier: Modifier = Modifier,
    uiState: DashboardUiState,
    onNavigateToDestination: NavigationCallback,
    onEvent: ValueChanged<DashboardUiEvent>
) {

    //navigation
    if (uiState.navigationTo != null) {
        onNavigateToDestination(uiState.navigationTo, null)
        onEvent(DashboardUiEvent.OnNavigated)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OverviewSlot(uiState = uiState)

        VerticalSpacer(height = 48.dp)

        MeterControlSlot { control ->
            onEvent(DashboardUiEvent.OnMeterControl(control))
        }
    }
}

@Preview
@Composable
private fun PreviewDashboardScreen() {
    MeterAppTheme {
        AppSurface {
            DashboardScreen(
                uiState = DashboardUiState(),
                onEvent = { },
                onNavigateToDestination = { _, _ -> }
            )
        }
    }
}

@Composable
private fun MeterControlSlot(
    modifier: Modifier = Modifier,
    onControl: ValueChanged<MeterControl>
) {

    TitleSlot(
        modifier = modifier
            .fillMaxWidth(),
        title = stringResource(R.string.controls)
    ) {
        MeterControlLazyList(onControl = onControl)
    }
}

@Composable
private fun MeterControlLazyList(
    modifier: Modifier = Modifier,
    onControl: ValueChanged<MeterControl>
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(
            items = MeterControl.entries,
            key = { control -> control.ordinal }
        ) { control ->
            MeterControlIcon(
                meterControl = control,
                onClick = onControl
            )
        }
    }
}

@Preview
@Composable
private fun PreviewMeterControlLazyList() {
    MeterAppTheme {
        AppSurface {
            MeterControlSlot(
                onControl = { }
            )
        }
    }
}