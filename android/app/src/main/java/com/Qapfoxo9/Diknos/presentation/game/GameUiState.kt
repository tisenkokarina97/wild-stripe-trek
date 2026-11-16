package com.Qapfoxo9.Diknos.presentation.game

import com.Qapfoxo9.Diknos.R
import com.Qapfoxo9.Diknos.domain.model.BoardState

data class GameUiState(
    val levelIndex: Int = 1,
    val roundId: Int = 0,
    val regionRes: Int = R.string.region_1,
    val board: BoardState = BoardState(),
    val treksLeft: Int = 0,
    val treksTotal: Int = 0,
    val found: Int = 0,
    val totalPawprints: Int = 0,
    val senseCharges: Int = 0,
    val senseUsed: Int = 0,
    val markMode: Boolean = false,
    val streak: Int = 0,
    val bestStreak: Int = 0,
    val assisted: Boolean = false,
    val inputLocked: Boolean = false,
    val paused: Boolean = false,
    val leaveRequested: Boolean = false,
    val started: Boolean = false
) {
    val pawprintProgress: Float
        get() = if (totalPawprints == 0) 0f else found.toFloat() / totalPawprints.toFloat()

    val lowOnTreks: Boolean get() = treksLeft in 1..3
}
