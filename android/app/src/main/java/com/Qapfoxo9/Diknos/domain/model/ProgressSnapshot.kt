package com.Qapfoxo9.Diknos.domain.model

import com.Qapfoxo9.Diknos.core.config.GameConfig

data class ProgressSnapshot(
    val unlockedLevel: Int = 1,
    val stripes: List<Int> = List(GameConfig.LEVEL_COUNT) { 0 },
    val journal: List<Int> = List(GameConfig.LEVEL_COUNT) { GameConfig.JOURNAL_LOCKED },
    val totalPawprints: Int = 0,
    val treksWalked: Int = 0,
    val treksSaved: Int = 0,
    val bestStreak: Int = 0,
    val roundsPlayed: Int = 0,
    val roundsWon: Int = 0
) {
    val trailsCleared: Int get() = stripes.count { it > 0 }
    val journalPages: Int get() = journal.count { it != GameConfig.JOURNAL_LOCKED }
    val goldenPages: Int get() = journal.count { it == GameConfig.JOURNAL_GOLDEN }
    val stripesEarned: Int get() = stripes.sum()
    val clearRate: Int get() = if (roundsPlayed == 0) 0 else (roundsWon * 100) / roundsPlayed
    val hasProgress: Boolean get() = roundsPlayed > 0
}
