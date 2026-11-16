package com.Qapfoxo9.Diknos.presentation.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Qapfoxo9.Diknos.R
import com.Qapfoxo9.Diknos.core.di.ServiceLocator
import com.Qapfoxo9.Diknos.core.theme.AccentStripe
import com.Qapfoxo9.Diknos.core.theme.AccentTrail
import com.Qapfoxo9.Diknos.core.theme.BgBase
import com.Qapfoxo9.Diknos.core.theme.BgDeep
import com.Qapfoxo9.Diknos.core.theme.Danger
import com.Qapfoxo9.Diknos.core.theme.Hairline
import com.Qapfoxo9.Diknos.core.theme.SurfaceMuted
import com.Qapfoxo9.Diknos.core.theme.SurfaceRaised
import com.Qapfoxo9.Diknos.core.theme.TextPrimary
import com.Qapfoxo9.Diknos.core.theme.TextSecondary
import com.Qapfoxo9.Diknos.core.ui.ScreenScaffold
import com.Qapfoxo9.Diknos.domain.model.DefaultTap
import com.Qapfoxo9.Diknos.presentation.dialog.ConfirmResetDialog

@Composable
fun SettingsRoute(onBack: () -> Unit) {
    val settingsViewModel: SettingsViewModel = viewModel(factory = ServiceLocator.factory)
    val state by settingsViewModel.state.collectAsStateWithLifecycle()
    SettingsScreen(
        state = state,
        onBack = onBack,
        onSelectTap = { tap -> settingsViewModel.selectDefaultTap(tap) },
        onSafeFirstStep = { enabled -> settingsViewModel.setSafeFirstStep(enabled) },
        onRequestReset = { settingsViewModel.requestReset() },
        onCancelReset = { settingsViewModel.cancelReset() },
        onConfirmReset = { settingsViewModel.confirmReset() }
    )
}

@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onBack: () -> Unit,
    onSelectTap: (DefaultTap) -> Unit,
    onSafeFirstStep: (Boolean) -> Unit,
    onRequestReset: () -> Unit,
    onCancelReset: () -> Unit,
    onConfirmReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    val safeState = stringResource(
        if (state.safeFirstStep) R.string.state_on else R.string.state_off
    )
    ScreenScaffold(
        titleRes = R.string.settings,
        onBack = onBack,
        overlay = listOf(
            BgBase.copy(alpha = 0.66f),
            BgBase.copy(alpha = 0.80f),
            BgDeep.copy(alpha = 0.92f)
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SettingRow(
                label = stringResource(R.string.default_tap),
                explanation = stringResource(R.string.default_tap_hint)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TapChip(
                        label = stringResource(R.string.reveal),
                        selected = state.defaultTap == DefaultTap.REVEAL,
                        iconRes = R.drawable.ic_check,
                        onClick = { onSelectTap(DefaultTap.REVEAL) }
                    )
                    TapChip(
                        label = stringResource(R.string.mark),
                        selected = state.defaultTap == DefaultTap.MARK,
                        spriteRes = R.drawable.sprite_5,
                        onClick = { onSelectTap(DefaultTap.MARK) }
                    )
                }
            }
            SettingRow(
                label = stringResource(R.string.safe_first_step),
                explanation = stringResource(R.string.safe_first_hint)
            ) {
                Switch(
                    checked = state.safeFirstStep,
                    onCheckedChange = onSafeFirstStep,
                    modifier = Modifier.semantics { stateDescription = safeState },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = BgBase,
                        checkedTrackColor = AccentTrail,
                        uncheckedThumbColor = TextSecondary,
                        uncheckedTrackColor = SurfaceMuted
                    )
                )
            }
            SettingRow(
                label = stringResource(R.string.reset_progress),
                explanation = stringResource(R.string.reset_progress_hint)
            ) {
                OutlinedButton(
                    onClick = onRequestReset,
                    modifier = Modifier.defaultMinSize(minHeight = 48.dp),
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.5.dp, Danger),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Danger
                    )
                ) {
                    Text(
                        text = stringResource(R.string.reset),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
            if (state.erasedMessageVisible) {
                Text(
                    text = stringResource(R.string.expedition_erased),
                    style = MaterialTheme.typography.labelSmall,
                    color = AccentStripe,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
    if (state.resetDialogVisible) {
        ConfirmResetDialog(
            onConfirm = onConfirmReset,
            onCancel = onCancelReset
        )
    }
}

@Composable
private fun TapChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    iconRes: Int? = null,
    spriteRes: Int? = null
) {
    val shape = RoundedCornerShape(16.dp)
    val selectedLabel = stringResource(R.string.sd_selected)
    Button(
        onClick = onClick,
        modifier = Modifier
            .defaultMinSize(minHeight = 48.dp)
            .height(48.dp)
            .background(if (selected) AccentStripe else SurfaceRaised, shape)
            .semantics {
                if (selected) stateDescription = selectedLabel
            },
        shape = shape,
        border = BorderStroke(1.5.dp, if (selected) AccentStripe else Hairline),
        contentPadding = PaddingValues(horizontal = 12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = if (selected) BgBase else TextPrimary
        )
    ) {
        if (spriteRes != null) {
            Image(
                painter = painterResource(spriteRes),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(6.dp))
        } else if (iconRes != null && selected) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
