package com.example.blemeter.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import com.example.blemeter.R
import com.example.blemeter.utils.VoidCallback

@Composable
fun BLEMeterAlertDialog(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    positiveButtonText: String = stringResource(R.string.ok),
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
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                )
            },
            text = {
                Text(
                    text = description,
                    style = MaterialTheme.typography.labelMedium
                )

            },
            confirmButton = {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        showDialog = false
                        onConfirmation()
                    }
                ) {
                    Text(
                        text = positiveButtonText,
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1
                    )
                }
            },
            dismissButton = {
                if (negativeButtonText != null) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {
                            showDialog = false
                            onDismiss()
                        }
                    ) {
                        Text(
                            text = negativeButtonText,
                            style = MaterialTheme.typography.labelMedium,
                            maxLines = 1
                        )
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