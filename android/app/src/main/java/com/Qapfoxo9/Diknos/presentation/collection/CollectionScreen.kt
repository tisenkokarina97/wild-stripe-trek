package com.Qapfoxo9.Diknos.presentation.collection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Qapfoxo9.Diknos.R
import com.Qapfoxo9.Diknos.core.config.GameConfig
import com.Qapfoxo9.Diknos.core.di.ServiceLocator
import com.Qapfoxo9.Diknos.core.theme.BgBase
import com.Qapfoxo9.Diknos.core.theme.BgDeep
import com.Qapfoxo9.Diknos.core.theme.Gold
import com.Qapfoxo9.Diknos.core.ui.EmptyStateCard
import com.Qapfoxo9.Diknos.core.ui.ScreenScaffold
import com.Qapfoxo9.Diknos.domain.model.JournalPage
import com.Qapfoxo9.Diknos.domain.model.JournalState
import com.Qapfoxo9.Diknos.presentation.dialog.JournalPageDialog
import kotlin.math.floor

private const val JOURNAL_COLS = 3
private const val JOURNAL_ROWS = 4
private const val JOURNAL_GAP_DP = 12f

@Composable
fun CollectionRoute(onBack: () -> Unit) {
    val collectionViewModel: CollectionViewModel = viewModel(factory = ServiceLocator.factory)
    val state by collectionViewModel.state.collectAsStateWithLifecycle()
    CollectionScreen(
        state = state,
        onBack = onBack,
        onOpenPage = { index -> collectionViewModel.openPage(index) },
        onLockedTap = { index -> collectionViewModel.onLockedTap(index) },
        onClosePage = { collectionViewModel.closePage() }
    )
}

@Composable
fun CollectionScreen(
    state: CollectionUiState,
    onBack: () -> Unit,
    onOpenPage: (Int) -> Unit,
    onLockedTap: (Int) -> Unit,
    onClosePage: () -> Unit,
    modifier: Modifier = Modifier
) {
    ScreenScaffold(
        titleRes = R.string.expedition_journal,
        onBack = onBack,
        overlay = listOf(
            BgBase.copy(alpha = 0.60f),
            BgBase.copy(alpha = 0.78f),
            BgDeep.copy(alpha = 0.90f)
        ),
        modifier = modifier,
        trailing = {
            Text(
                text = stringResource(
                    R.string.fraction,
                    state.recovered,
                    GameConfig.LEVEL_COUNT
                ),
                style = MaterialTheme.typography.labelLarge,
                color = Gold,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    ) {
        if (state.recovered == 0) {
            EmptyStateCard(
                message = stringResource(R.string.journal_empty),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }
        if (state.lockedMessageLevel > 0) {
            Text(
                text = stringResource(R.string.level_locked_message, state.lockedMessageLevel),
                style = MaterialTheme.typography.labelSmall,
                color = Gold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            val cardWidth: Dp = floor(
                (maxWidth.value - JOURNAL_GAP_DP * (JOURNAL_COLS - 1)) / JOURNAL_COLS
            ).dp
            Column(
                modifier = Modifier.align(Alignment.TopCenter),
                verticalArrangement = Arrangement.spacedBy(JOURNAL_GAP_DP.dp)
            ) {
                for (row in 0 until JOURNAL_ROWS) {
                    Row(horizontalArrangement = Arrangement.spacedBy(JOURNAL_GAP_DP.dp)) {
                        for (col in 0 until JOURNAL_COLS) {
                            val position = row * JOURNAL_COLS + col
                            val page: JournalPage? = state.pages.getOrNull(position)
                            if (page != null) {
                                JournalCard(
                                    page = page,
                                    width = cardWidth,
                                    onOpen = { onOpenPage(page.index) },
                                    onLockedTap = { onLockedTap(page.index) }
                                )
                            } else {
                                Spacer(modifier = Modifier.size(cardWidth))
                            }
                        }
                    }
                }
            }
        }
    }
    val selected = state.selected
    if (selected != null) {
        JournalPageDialog(
            titleRes = selected.titleRes,
            logRes = selected.logRes,
            golden = selected.state == JournalState.GOLDEN,
            onClose = onClosePage
        )
    }
}
