package com.Qapfoxo9.Diknos.data.sample

import com.Qapfoxo9.Diknos.R
import com.Qapfoxo9.Diknos.core.config.GameConfig
import com.Qapfoxo9.Diknos.domain.model.TrekLevel

object SampleData {

    val levels: List<TrekLevel> = listOf(
        TrekLevel(1, R.string.region_1, 3, 3, 22),
        TrekLevel(2, R.string.region_2, 3, 4, 21),
        TrekLevel(3, R.string.region_3, 4, 5, 20),
        TrekLevel(4, R.string.region_4, 4, 6, 19),
        TrekLevel(5, R.string.region_5, 5, 6, 19),
        TrekLevel(6, R.string.region_6, 5, 7, 18),
        TrekLevel(7, R.string.region_7, 6, 7, 18),
        TrekLevel(8, R.string.region_8, 6, 8, 17),
        TrekLevel(9, R.string.region_9, 7, 8, 17),
        TrekLevel(10, R.string.region_10, 7, 9, 16),
        TrekLevel(11, R.string.region_11, 8, 9, 16),
        TrekLevel(12, R.string.region_12, 8, 10, 15)
    )

    val journalTitles: List<Int> = listOf(
        R.string.journal_title_1,
        R.string.journal_title_2,
        R.string.journal_title_3,
        R.string.journal_title_4,
        R.string.journal_title_5,
        R.string.journal_title_6,
        R.string.journal_title_7,
        R.string.journal_title_8,
        R.string.journal_title_9,
        R.string.journal_title_10,
        R.string.journal_title_11,
        R.string.journal_title_12
    )

    val journalLogs: List<Int> = listOf(
        R.string.journal_log_1,
        R.string.journal_log_2,
        R.string.journal_log_3,
        R.string.journal_log_4,
        R.string.journal_log_5,
        R.string.journal_log_6,
        R.string.journal_log_7,
        R.string.journal_log_8,
        R.string.journal_log_9,
        R.string.journal_log_10,
        R.string.journal_log_11,
        R.string.journal_log_12
    )

    fun level(index: Int): TrekLevel =
        levels[(index - 1).coerceIn(0, GameConfig.LEVEL_COUNT - 1)]
}
