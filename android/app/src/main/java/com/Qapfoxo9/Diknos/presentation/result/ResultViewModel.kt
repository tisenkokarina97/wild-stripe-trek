package com.Qapfoxo9.Diknos.presentation.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Qapfoxo9.Diknos.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ResultViewModel(
    progress: ProgressRepository
) : ViewModel() {

    val state: StateFlow<ResultUiState> = progress.snapshot
        .map { trek ->
            ResultUiState(
                lifetimeBestStreak = trek.bestStreak,
                journalPages = trek.journalPages,
                goldenPages = trek.goldenPages
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ResultUiState()
        )
}
