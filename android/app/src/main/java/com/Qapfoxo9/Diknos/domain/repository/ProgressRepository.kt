package com.Qapfoxo9.Diknos.domain.repository

import com.Qapfoxo9.Diknos.domain.model.ProgressSnapshot
import com.Qapfoxo9.Diknos.domain.model.RoundRecord
import kotlinx.coroutines.flow.StateFlow

interface ProgressRepository {

    val snapshot: StateFlow<ProgressSnapshot>

    fun recordRound(record: RoundRecord)

    fun reset()
}
