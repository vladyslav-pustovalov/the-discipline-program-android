package my.vladpustovalov.thedisciplineprogram.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import my.vladpustovalov.thedisciplineprogram.R

@Composable
fun ErrorAlertDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    confirmText: String = stringResource(R.string.common_ok)
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message.ifEmpty { title }) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(confirmText)
            }
        }
    )
}
