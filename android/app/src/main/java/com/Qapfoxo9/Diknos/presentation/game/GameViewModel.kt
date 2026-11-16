package com.Qapfoxo9.Diknos.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Qapfoxo9.Diknos.core.config.GameConfig
import com.Qapfoxo9.Diknos.core.navigation.Screen
import com.Qapfoxo9.Diknos.data.sample.SampleData
import com.Qapfoxo9.Diknos.domain.model.DefaultTap
import com.Qapfoxo9.Diknos.domain.model.RoundOutcome
import com.Qapfoxo9.Diknos.domain.model.RoundRecord
import com.Qapfoxo9.Diknos.domain.model.TileKind
import com.Qapfoxo9.Diknos.domain.repository.ProgressRepository
import com.Qapfoxo9.Diknos.domain.repository.SettingsRepository
import com.Qapfoxo9.Diknos.domain.usecase.GenerateBoardUseCase
import com.Qapfoxo9.Diknos.domain.usecase.RevealTileUseCase
import com.Qapfoxo9.Diknos.domain.usecase.ScoreRoundUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.random.Random

class GameViewModel(
    private val progress: ProgressRepository,
    private val settings: SettingsRepository,
    private val generateBoard: GenerateBoardUseCase,
    private val revealTile: RevealTileUseCase,
    private val scoreRound: ScoreRoundUseCase
) : ViewModel() {

    private val uiState = MutableStateFlow(GameUiState())

    val state: StateFlow<GameUiState> = uiState.asStateFlow()

    private val finished = Channel<Screen.Result>(Channel.BUFFERED)

    val roundFinished: Flow<Screen.Result> = finished.receiveAsFlow()

    private var resolveJob: Job? = null

    fun prepareRound(levelIndex: Int, roundId: Int) {
        val current = uiState.value
        if (current.started && current.roundId == roundId) return
        resolveJob?.cancel()
        val level = SampleData.level(levelIndex)
        val options = settings.snapshot.value
        val board = generateBoard(level, roundId + 1, options.safeFirstStep)
        uiState.value = GameUiState(
            levelIndex = level.index,
            roundId = roundId,
            regionRes = level.regionRes,
            board = board,
            treksLeft = level.treks,
            treksTotal = level.treks,
            totalPawprints = level.pawprints,
            senseCharges = 1,
            markMode = options.defaultTap == DefaultTap.MARK,
            started = true
        )
    }

    fun onTileTap(index: Int) {
        val current = uiState.value
        if (current.inputLocked || current.paused || current.leaveRequested) return
        val tile = current.board.tiles.getOrNull(index) ?: return
        if (tile.revealed) return

        if (current.markMode) {
            val tiles = current.board.tiles.toMutableList()
            tiles[index] = tile.copy(marked = !tile.marked)
            uiState.update { it.copy(board = it.board.withTiles(tiles)) }
            return
        }
        if (tile.marked || current.treksLeft <= 0) return

        val board = revealTile(current.board, index)
        val treksLeft = current.treksLeft - 1

        if (tile.kind == TileKind.NEST) {
            uiState.update {
                it.copy(
                    board = board.revealAllNests(),
                    treksLeft = treksLeft,
                    streak = 0,
                    inputLocked = true
                )
            }
            resolve(RoundOutcome.NEST)
            return
        }

        val found = board.pawprintsFound
        val streak = current.streak + 1
        val best = max(current.bestStreak, streak)
        val charges = chargesFor(found, current.senseUsed)
        val complete = found >= current.totalPawprints
        val cold = !complete && treksLeft <= 0
        uiState.update {
            it.copy(
                board = board,
                treksLeft = treksLeft,
                found = found,
                streak = streak,
                bestStreak = best,
                senseCharges = charges,
                inputLocked = complete || cold
            )
        }
        when {
            complete -> resolve(RoundOutcome.COMPLETE)
            cold -> resolve(RoundOutcome.COLD)
        }
    }

    fun toggleMarkMode() {
        val current = uiState.value
        if (current.inputLocked || current.paused) return
        uiState.update { it.copy(markMode = !it.markMode) }
    }

    fun useTigerSense() {
        val current = uiState.value
        if (current.inputLocked || current.paused || current.senseCharges <= 0) return
        val candidates = current.board.tiles.indices.filter { position ->
            val tile = current.board.tiles[position]
            !tile.revealed && tile.kind != TileKind.NEST
        }
        if (candidates.isEmpty()) return
        val picker = Random(current.board.seed + current.treksLeft * 31L + current.senseUsed * 17L)
        val chosen = candidates.random(picker)
        val cleared = current.board.withTiles(
            current.board.tiles.mapIndexed { position, tile ->
                if (position == chosen) tile.copy(marked = false) else tile
            }
        )
        val board = revealTile(cleared, chosen)
        val found = board.pawprintsFound
        val senseUsed = current.senseUsed + 1
        val complete = found >= current.totalPawprints
        uiState.update {
            it.copy(
                board = board,
                found = found,
                senseUsed = senseUsed,
                senseCharges = chargesFor(found, senseUsed),
                streak = 0,
                assisted = true,
                inputLocked = complete
            )
        }
        if (complete) resolve(RoundOutcome.COMPLETE)
    }

    fun openPause() {
        if (uiState.value.inputLocked) return
        uiState.update { it.copy(paused = true) }
    }

    fun closePause() {
        uiState.update { it.copy(paused = false) }
    }

    fun requestLeave() {
        if (uiState.value.inputLocked) return
        uiState.update { it.copy(leaveRequested = true) }
    }

    fun dismissLeave() {
        uiState.update { it.copy(leaveRequested = false) }
    }

    fun abandonRound() {
        resolveJob?.cancel()
        uiState.value = GameUiState()
    }

    fun pause() {
        resolveJob?.cancel()
    }

    private fun chargesFor(found: Int, senseUsed: Int): Int =
        (1 + found / GameConfig.SENSE_PER_PAWPRINTS - senseUsed)
            .coerceIn(0, GameConfig.MAX_SENSE)

    private fun resolve(outcome: RoundOutcome) {
        resolveJob?.cancel()
        resolveJob = viewModelScope.launch {
            delay(GameConfig.RESOLVE_DELAY_MS)
            val round = uiState.value
            val options = settings.snapshot.value
            val won = outcome == RoundOutcome.COMPLETE
            val stripes = if (won) scoreRound.stripesFor(round.treksLeft, round.treksTotal) else 0
            val golden = won && stripes == 3 && !round.assisted
            val previousJournal = progress.snapshot.value.journal
                .getOrElse(round.levelIndex - 1) { GameConfig.JOURNAL_LOCKED }
            val award = when {
                !won -> GameConfig.JOURNAL_LOCKED
                golden && previousJournal < GameConfig.JOURNAL_GOLDEN -> GameConfig.JOURNAL_GOLDEN
                previousJournal == GameConfig.JOURNAL_LOCKED -> GameConfig.JOURNAL_RECOVERED
                else -> GameConfig.JOURNAL_LOCKED
            }
            progress.recordRound(
                RoundRecord(
                    levelIndex = round.levelIndex,
                    outcome = outcome,
                    pawprintsFound = round.found,
                    treksUsed = round.treksTotal - round.treksLeft,
                    treksLeft = round.treksLeft,
                    bestStreak = round.bestStreak,
                    stripes = stripes,
                    golden = golden
                )
            )
            val score = scoreRound(
                found = round.found,
                treksLeft = round.treksLeft,
                bestStreak = round.bestStreak,
                assisted = round.assisted,
                safeFirstStep = options.safeFirstStep
            )
            finished.send(
                Screen.Result(
                    levelIndex = round.levelIndex,
                    outcome = outcome,
                    found = round.found,
                    total = round.totalPawprints,
                    treksLeft = round.treksLeft,
                    bestStreak = round.bestStreak,
                    score = score,
                    stripes = stripes,
                    journalAward = award,
                    unlockedNext = won && round.levelIndex < GameConfig.LEVEL_COUNT,
                    hardTrailBonus = !options.safeFirstStep
                )
            )
        }
    }
}
