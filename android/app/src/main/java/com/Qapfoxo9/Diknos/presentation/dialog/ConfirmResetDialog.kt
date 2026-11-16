package com.Qapfoxo9.Diknos.presentation.dialog

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Qapfoxo9.Diknos.R
import com.Qapfoxo9.Diknos.core.theme.Danger
import com.Qapfoxo9.Diknos.core.theme.SurfaceBase
import com.Qapfoxo9.Diknos.core.theme.TextPrimary
import com.Qapfoxo9.Diknos.core.theme.TextSecondary
import com.Qapfoxo9.Diknos.core.ui.PrimaryButton
import com.Qapfoxo9.Diknos.core.ui.SecondaryButton

@Composable
fun ConfirmResetDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        confirmButton = {
            PrimaryButton(
                text = stringResource(R.string.cancel),
                onClick = onCancel,
                height = 50.dp
            )
        },
        dismissButton = {
            SecondaryButton(
                text = stringResource(R.string.reset),
                onClick = onConfirm,
                accent = Danger
            )
        },
        title = {
            Text(
                text = stringResource(R.string.confirm_reset_title),
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
        },
        text = {
            Text(
                text = stringResource(R.string.confirm_reset_text),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        },
        shape = RoundedCornerShape(28.dp),
        containerColor = SurfaceBase
    )
}
