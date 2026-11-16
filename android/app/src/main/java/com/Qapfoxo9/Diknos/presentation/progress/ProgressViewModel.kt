package com.Qapfoxo9.Diknos.presentation.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Qapfoxo9.Diknos.core.config.GameConfig
import com.Qapfoxo9.Diknos.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ProgressViewModel(
    progress: ProgressRepository
) : ViewModel() {

    val state: StateFlow<ProgressUiState> = progress.snapshot
        .map { trek ->
            ProgressUiState(
                trailsCleared = trek.trailsCleared,
                totalLevels = GameConfig.LEVEL_COUNT,
                pawprints = trek.totalPawprints,
                journalPages = trek.journalPages,
                goldenPages = trek.goldenPages,
                bestStreak = trek.bestStreak,
                clearRate = trek.clearRate,
                treksWalked = trek.treksWalked,
                treksSaved = trek.treksSaved,
                segments = trek.journal,
                hasProgress = trek.hasProgress
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProgressUiState()
        )
}
