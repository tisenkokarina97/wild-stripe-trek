package com.Qapfoxo9.Diknos.presentation.progress

import com.Qapfoxo9.Diknos.core.config.GameConfig

data class ProgressUiState(
    val trailsCleared: Int = 0,
    val totalLevels: Int = GameConfig.LEVEL_COUNT,
    val pawprints: Int = 0,
    val journalPages: Int = 0,
    val goldenPages: Int = 0,
    val bestStreak: Int = 0,
    val clearRate: Int = 0,
    val treksWalked: Int = 0,
    val treksSaved: Int = 0,
    val segments: List<Int> = List(GameConfig.LEVEL_COUNT) { GameConfig.JOURNAL_LOCKED },
    val hasProgress: Boolean = false
)
