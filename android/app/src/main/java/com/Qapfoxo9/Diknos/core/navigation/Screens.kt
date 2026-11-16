package com.Qapfoxo9.Diknos.core.navigation

import com.Qapfoxo9.Diknos.domain.model.RoundOutcome

sealed interface Screen {

    data object Splash : Screen

    data object Menu : Screen

    data object Levels : Screen

    data class Game(val levelIndex: Int, val roundId: Int) : Screen

    data class Result(
        val levelIndex: Int,
        val outcome: RoundOutcome,
        val found: Int,
        val total: Int,
        val treksLeft: Int,
        val bestStreak: Int,
        val score: Int,
        val stripes: Int,
        val journalAward: Int,
        val unlockedNext: Boolean,
        val hardTrailBonus: Boolean
    ) : Screen

    data object Journal : Screen

    data object Expedition : Screen

    data object Options : Screen
}
