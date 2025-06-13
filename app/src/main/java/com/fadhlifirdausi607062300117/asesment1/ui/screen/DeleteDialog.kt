package com.fadhlifirdausi607062300117.asesment1.ui.screen

import android.content.res.Configuration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.fadhlifirdausi607062300117.asesment1.R

@Composable
fun DeleteConfirmationDialog(
    onConfirmDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(id = R.string.delete)) },
        text = { Text(text = stringResource(id = R.string.delete_alert)) },
        confirmButton = {
            TextButton(onClick = onConfirmDelete) {
                Text(text = stringResource(R.string.delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DeleteConfirmationDialogPreview() {
    DeleteConfirmationDialog(
        onConfirmDelete = {},
        onDismiss = {}
    )
}