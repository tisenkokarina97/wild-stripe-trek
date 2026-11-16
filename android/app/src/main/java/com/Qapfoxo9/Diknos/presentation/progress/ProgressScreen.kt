package com.Qapfoxo9.Diknos.presentation.progress

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Qapfoxo9.Diknos.R
import com.Qapfoxo9.Diknos.core.di.ServiceLocator
import com.Qapfoxo9.Diknos.core.theme.AccentStripe
import com.Qapfoxo9.Diknos.core.theme.AccentTrail
import com.Qapfoxo9.Diknos.core.theme.BgBase
import com.Qapfoxo9.Diknos.core.theme.BgDeep
import com.Qapfoxo9.Diknos.core.theme.Gold
import com.Qapfoxo9.Diknos.core.theme.Hairline
import com.Qapfoxo9.Diknos.core.theme.TextPrimary
import com.Qapfoxo9.Diknos.core.theme.TextSecondary
import com.Qapfoxo9.Diknos.core.ui.ClayCard
import com.Qapfoxo9.Diknos.core.ui.EmptyStateCard
import com.Qapfoxo9.Diknos.core.ui.ScreenScaffold
import com.Qapfoxo9.Diknos.core.ui.SectionLabel
import com.Qapfoxo9.Diknos.core.ui.StatCard

@Composable
fun ProgressRoute(onBack: () -> Unit) {
    val progressViewModel: ProgressViewModel = viewModel(factory = ServiceLocator.factory)
    val state by progressViewModel.state.collectAsStateWithLifecycle()
    ProgressScreen(state = state, onBack = onBack)
}

@Composable
fun ProgressScreen(
    state: ProgressUiState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    ScreenScaffold(
        titleRes = R.string.progress,
        onBack = onBack,
        overlay = listOf(
            BgBase.copy(alpha = 0.60f),
            BgBase.copy(alpha = 0.78f),
            BgDeep.copy(alpha = 0.90f)
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (!state.hasProgress) {
                EmptyStateCard(message = stringResource(R.string.progress_empty))
            }
            StatPair(
                leftLabel = stringResource(R.string.trails_cleared),
                leftValue = stringResource(
                    R.string.fraction,
                    state.trailsCleared,
                    state.totalLevels
                ),
                leftAccent = AccentTrail,
                rightLabel = stringResource(R.string.pawprints),
                rightValue = state.pawprints.toString(),
                rightAccent = Gold
            )
            StatPair(
                leftLabel = stringResource(R.string.journal),
                leftValue = stringResource(
                    R.string.fraction,
                    state.journalPages,
                    state.totalLevels
                ),
                leftAccent = Gold,
                rightLabel = stringResource(R.string.golden_pages),
                rightValue = state.goldenPages.toString(),
                rightAccent = AccentStripe
            )
            StatPair(
                leftLabel = stringResource(R.string.best_streak),
                leftValue = state.bestStreak.toString(),
                leftAccent = AccentTrail,
                rightLabel = stringResource(R.string.clear_rate),
                rightValue = stringResource(R.string.percent_value, state.clearRate),
                rightAccent = TextPrimary
            )
            SectionLabel(text = stringResource(R.string.trail_by_trail))
            ExpeditionBar(segments = state.segments)
            TotalRow(
                label = stringResource(R.string.treks_walked),
                value = state.treksWalked.toString()
            )
            TotalRow(
                label = stringResource(R.string.treks_saved),
                value = state.treksSaved.toString()
            )
        }
    }
}

@Composable
private fun StatPair(
    leftLabel: String,
    leftValue: String,
    leftAccent: Color,
    rightLabel: String,
    rightValue: String,
    rightAccent: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            label = leftLabel,
            value = leftValue,
            accent = leftAccent,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            label = rightLabel,
            value = rightValue,
            accent = rightAccent,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TotalRow(label: String, value: String) {
    ClayCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        border = BorderStroke(1.5.dp, Hairline),
        corner = 20.dp,
        elevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
        }
    }
}
