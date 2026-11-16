package com.Qapfoxo9.Diknos.presentation.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Qapfoxo9.Diknos.R
import com.Qapfoxo9.Diknos.core.theme.Gold
import com.Qapfoxo9.Diknos.core.theme.SurfaceBase
import com.Qapfoxo9.Diknos.core.theme.TextPrimary
import com.Qapfoxo9.Diknos.core.theme.TextSecondary
import com.Qapfoxo9.Diknos.core.ui.PrimaryButton

@Composable
fun JournalPageDialog(
    titleRes: Int,
    logRes: Int,
    golden: Boolean,
    onClose: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = {
            PrimaryButton(
                text = stringResource(R.string.close),
                onClick = onClose,
                height = 50.dp
            )
        },
        icon = {
            Image(
                painter = painterResource(R.drawable.sprite_4),
                contentDescription = stringResource(R.string.cd_journal),
                modifier = Modifier.size(44.dp),
                contentScale = ContentScale.Fit
            )
        },
        title = {
            Text(
                text = stringResource(titleRes),
                style = MaterialTheme.typography.titleMedium,
                color = if (golden) Gold else TextPrimary
            )
        },
        text = {
            Text(
                text = stringResource(logRes),
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
        },
        shape = RoundedCornerShape(28.dp),
        containerColor = SurfaceBase
    )
}
