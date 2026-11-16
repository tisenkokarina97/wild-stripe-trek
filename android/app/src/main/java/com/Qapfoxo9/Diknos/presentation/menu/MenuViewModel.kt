package com.Qapfoxo9.Diknos.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Qapfoxo9.Diknos.data.sample.SampleData
import com.Qapfoxo9.Diknos.domain.repository.ProgressRepository
import com.Qapfoxo9.Diknos.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MenuViewModel(
    progress: ProgressRepository,
    settings: SettingsRepository
) : ViewModel() {

    val state: StateFlow<MenuUiState> =
        combine(progress.snapshot, settings.snapshot) { trek, options ->
            val level = SampleData.level(trek.unlockedLevel)
            MenuUiState(
                currentLevel = level.index,
                currentRegionRes = level.regionRes,
                totalPawprints = trek.totalPawprints,
                bestStreak = trek.bestStreak,
                journalPages = trek.journalPages,
                trailsCleared = trek.trailsCleared,
                clearRate = trek.clearRate,
                safeFirstStep = options.safeFirstStep,
                hasProgress = trek.hasProgress
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MenuUiState()
        )
}
