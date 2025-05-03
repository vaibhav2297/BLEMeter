@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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

@Composable
fun AppDialog(
    modifier: Modifier = Modifier,
    onDismiss: VoidCallback,
    content: @Composable ColumnScope.() -> Unit
) {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        Dialog(
            onDismissRequest = {
                showDialog = false
                onDismiss()
            },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(
                        color = AppTheme.colors.background,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(
                        bottom = AppTheme.padding.large,
                        start = AppTheme.padding.large,
                        end = AppTheme.padding.large,
                        top = AppTheme.padding.extraLarge
                    ),
                content = content
            )
        }
    }
}

@Preview
@Composable
private fun PreviewAppAlertDialog() {
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


@Preview
@Composable
private fun PreviewAppDialog() {
    MeterAppTheme {
        AppSurface {
            AppDialog(
                modifier = Modifier,
                onDismiss = { }
            ) {

            }
        }
    }
}


@Composable
fun AppBottomSheet(
    modifier: Modifier = Modifier,
    onDismiss: VoidCallback,
    sheetState: SheetState = rememberModalBottomSheetState(),
    content: @Composable ColumnScope.() -> Unit
) {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        ModalBottomSheet(
            modifier = modifier
                .fillMaxWidth(),
            sheetState = sheetState,
            shape = MaterialTheme.shapes.medium,
            containerColor = AppTheme.colors.background,
            onDismissRequest = {
                showDialog = false
                onDismiss()
            },
            content = content
        )
    }
}


@Preview
@Composable
private fun PreviewAppBottomSheet() {
    MeterAppTheme {
        AppSurface {
            AppBottomSheet(onDismiss = {

            }) {

            }
        }
    }
}
