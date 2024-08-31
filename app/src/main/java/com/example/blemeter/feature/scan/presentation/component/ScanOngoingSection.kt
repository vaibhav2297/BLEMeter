package com.example.blemeter.feature.scan.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.blemeter.R
import com.example.blemeter.ui.components.AppOutlinedButton
import com.example.blemeter.ui.components.AppSurface
import com.example.blemeter.ui.components.ButtonState
import com.example.blemeter.ui.theme.MeterAppTheme
import com.example.blemeter.utils.VoidCallback

@Composable
fun ScanOngoingSection(
    modifier: Modifier = Modifier,
    onCancel: VoidCallback
) {
    ScanScreenSlot(
        modifier = modifier,
        topContent = { mod ->
            InformationSection(
                modifier = mod,
                title = stringResource(
                    id = R.string.searching_for_devices
                ),
                description = stringResource(
                    R.string.meter_is_nearby_desc
                )
            )
        },
        centerContent = { mod ->
            ScanningLottieView(modifier = mod)
        }
    ) { mod ->
        AppOutlinedButton(
            modifier = mod,
            text = stringResource(R.string.cancel),
            buttonState = ButtonState.ACTIVE,
            onClick = onCancel
        )
    }
}

@Preview
@Composable
private fun PreviewScanOngoingSection() {
    MeterAppTheme {
        AppSurface {
            ScanOngoingSection(
                onCancel = { }
            )
        }
    }
}

@Composable
private fun ScanningLottieView(
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.searching)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        speed = 0.5f,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        modifier = modifier,
        alignment = Alignment.Center,
        composition = composition,
        progress = { progress }
    )
}

