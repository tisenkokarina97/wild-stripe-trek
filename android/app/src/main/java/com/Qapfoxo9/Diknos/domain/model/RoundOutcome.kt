package com.Qapfoxo9.Diknos.domain.model

enum class RoundOutcome { COMPLETE, NEST, COLD }

data class RoundRecord(
    val levelIndex: Int,
    val outcome: RoundOutcome,
    val pawprintsFound: Int,
    val treksUsed: Int,
    val treksLeft: Int,
    val bestStreak: Int,
    val stripes: Int,
    val golden: Boolean
)

data class RoundResult(
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
)
