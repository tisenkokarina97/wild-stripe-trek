package com.Qapfoxo9.Diknos.presentation.menu

import com.Qapfoxo9.Diknos.R

data class MenuUiState(
    val currentLevel: Int = 1,
    val currentRegionRes: Int = R.string.region_1,
    val totalPawprints: Int = 0,
    val bestStreak: Int = 0,
    val journalPages: Int = 0,
    val trailsCleared: Int = 0,
    val clearRate: Int = 0,
    val safeFirstStep: Boolean = true,
    val hasProgress: Boolean = false
)
