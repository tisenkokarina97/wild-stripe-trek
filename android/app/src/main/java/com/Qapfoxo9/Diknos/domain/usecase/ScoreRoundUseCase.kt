package com.Qapfoxo9.Diknos.domain.usecase

import com.Qapfoxo9.Diknos.core.config.GameConfig
import kotlin.math.floor

class ScoreRoundUseCase {

    operator fun invoke(
        found: Int,
        treksLeft: Int,
        bestStreak: Int,
        assisted: Boolean,
        safeFirstStep: Boolean
    ): Int {
        val base = found * GameConfig.SCORE_PER_PAWPRINT +
            treksLeft * GameConfig.SCORE_PER_TREK_LEFT +
            bestStreak * GameConfig.SCORE_PER_STREAK
        val boosted = if (assisted) {
            base
        } else {
            floor(base * GameConfig.SCORE_UNASSISTED_MULTIPLIER).toInt()
        }
        return if (safeFirstStep) boosted else boosted + GameConfig.SCORE_HARD_TRAIL_BONUS
    }

    fun stripesFor(treksLeft: Int, treksTotal: Int): Int {
        if (treksTotal <= 0) return 1
        val ratio = treksLeft.toFloat() / treksTotal.toFloat()
        return when {
            ratio >= GameConfig.STRIPE_THREE_RATIO -> 3
            ratio >= GameConfig.STRIPE_TWO_RATIO -> 2
            else -> 1
        }
    }
}
