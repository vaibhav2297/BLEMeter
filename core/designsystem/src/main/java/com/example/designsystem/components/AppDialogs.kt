@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.designsystem.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.designsystem.R
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.theme.MeterAppTheme
import com.example.designsystem.theme.VoidCallback

@Composable
fun AppAlertDialog(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    positiveButtonText: String = stringResource(id = R.string.ok),
    negativeButtonText: String? = null,
    onDismiss: VoidCallback,
    onConfirmation: VoidCallback
) {

    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            modifier = modifier,
            onDismissRequest = onDismiss,
            shape = MaterialTheme.shapes.medium,
            containerColor = AppTheme.colors.background,
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontSize = 18.sp
                    ),
                    color = AppTheme.colors.textPrimary
                )
            },
            text = {
                Text(
                    text = description,
                    style = MaterialTheme.typography.labelSmall,
                    color = AppTheme.colors.textHighlighted
                )

            },
            confirmButton = {
                AppOutlinedButton(
                    text = positiveButtonText
                ) {
                    showDialog = false
                    onConfirmation()
                }
            },
            dismissButton = {
                if (negativeButtonText != null) {
                    AppOutlinedButton(
                        text = negativeButtonText
                    ) {
                        showDialog = false
                        onDismiss()
                    }
                }
            },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        )
    }
}

@Preview
@Composable
private fun PreviewAppDialog() {
    MeterAppTheme {
        AppSurface {
            AppAlertDialog(
                modifier = Modifier,
                title = "This is an alert title",
                description = "here goes the description",
                positiveButtonText = "Yes",
                negativeButtonText = "No",
                onDismiss = { },
                onConfirmation = { }
            )
        }
    }
}
