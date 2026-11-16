package com.Qapfoxo9.Diknos.data.repository

import com.Qapfoxo9.Diknos.core.config.GameConfig
import com.Qapfoxo9.Diknos.data.local.TrekPreferences
import com.Qapfoxo9.Diknos.domain.model.ProgressSnapshot
import com.Qapfoxo9.Diknos.domain.model.RoundOutcome
import com.Qapfoxo9.Diknos.domain.model.RoundRecord
import com.Qapfoxo9.Diknos.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.max

class ProgressRepositoryImpl(
    private val prefs: TrekPreferences
) : ProgressRepository {

    private val state = MutableStateFlow(read())

    override val snapshot: StateFlow<ProgressSnapshot> = state.asStateFlow()

    override fun recordRound(record: RoundRecord) {
        val won = record.outcome == RoundOutcome.COMPLETE
        prefs.writeInt(
            TrekPreferences.KEY_ROUNDS_PLAYED,
            prefs.readInt(TrekPreferences.KEY_ROUNDS_PLAYED, 0) + 1
        )
        prefs.writeInt(
            TrekPreferences.KEY_TOTAL_PAWPRINTS,
            prefs.readInt(TrekPreferences.KEY_TOTAL_PAWPRINTS, 0) + record.pawprintsFound
        )
        prefs.writeInt(
            TrekPreferences.KEY_TOTAL_TREKS,
            prefs.readInt(TrekPreferences.KEY_TOTAL_TREKS, 0) + record.treksUsed
        )
        prefs.writeInt(
            TrekPreferences.KEY_BEST_STREAK,
            max(prefs.readInt(TrekPreferences.KEY_BEST_STREAK, 0), record.bestStreak)
        )
        if (won) {
            prefs.writeInt(
                TrekPreferences.KEY_ROUNDS_WON,
                prefs.readInt(TrekPreferences.KEY_ROUNDS_WON, 0) + 1
            )
            prefs.writeInt(
                TrekPreferences.KEY_TREKS_SAVED,
                prefs.readInt(TrekPreferences.KEY_TREKS_SAVED, 0) + record.treksLeft
            )
            val stripeKey = TrekPreferences.KEY_STRIPES + record.levelIndex
            prefs.writeInt(stripeKey, max(prefs.readInt(stripeKey, 0), record.stripes))
            val journalKey = TrekPreferences.KEY_JOURNAL + record.levelIndex
            val award = if (record.golden) GameConfig.JOURNAL_GOLDEN else GameConfig.JOURNAL_RECOVERED
            prefs.writeInt(journalKey, max(prefs.readInt(journalKey, GameConfig.JOURNAL_LOCKED), award))
            val unlocked = prefs.readInt(TrekPreferences.KEY_UNLOCKED_LEVEL, 1)
            val next = (record.levelIndex + 1).coerceAtMost(GameConfig.LEVEL_COUNT)
            prefs.writeInt(TrekPreferences.KEY_UNLOCKED_LEVEL, max(unlocked, next))
        }
        state.value = read()
    }

    override fun reset() {
        val keys = mutableListOf(
            TrekPreferences.KEY_UNLOCKED_LEVEL,
            TrekPreferences.KEY_TOTAL_PAWPRINTS,
            TrekPreferences.KEY_TOTAL_TREKS,
            TrekPreferences.KEY_TREKS_SAVED,
            TrekPreferences.KEY_BEST_STREAK,
            TrekPreferences.KEY_ROUNDS_PLAYED,
            TrekPreferences.KEY_ROUNDS_WON
        )
        for (level in 1..GameConfig.LEVEL_COUNT) {
            keys.add(TrekPreferences.KEY_STRIPES + level)
            keys.add(TrekPreferences.KEY_JOURNAL + level)
        }
        prefs.remove(keys)
        state.value = read()
    }

    private fun read(): ProgressSnapshot = ProgressSnapshot(
        unlockedLevel = prefs.readInt(TrekPreferences.KEY_UNLOCKED_LEVEL, 1)
            .coerceIn(1, GameConfig.LEVEL_COUNT),
        stripes = List(GameConfig.LEVEL_COUNT) {
            prefs.readInt(TrekPreferences.KEY_STRIPES + (it + 1), 0)
        },
        journal = List(GameConfig.LEVEL_COUNT) {
            prefs.readInt(TrekPreferences.KEY_JOURNAL + (it + 1), GameConfig.JOURNAL_LOCKED)
        },
        totalPawprints = prefs.readInt(TrekPreferences.KEY_TOTAL_PAWPRINTS, 0),
        treksWalked = prefs.readInt(TrekPreferences.KEY_TOTAL_TREKS, 0),
        treksSaved = prefs.readInt(TrekPreferences.KEY_TREKS_SAVED, 0),
        bestStreak = prefs.readInt(TrekPreferences.KEY_BEST_STREAK, 0),
        roundsPlayed = prefs.readInt(TrekPreferences.KEY_ROUNDS_PLAYED, 0),
        roundsWon = prefs.readInt(TrekPreferences.KEY_ROUNDS_WON, 0)
    )
}
