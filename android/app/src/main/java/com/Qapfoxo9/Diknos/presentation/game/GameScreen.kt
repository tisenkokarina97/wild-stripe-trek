package com.Qapfoxo9.Diknos.presentation.game

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Qapfoxo9.Diknos.R
import com.Qapfoxo9.Diknos.core.config.GameConfig
import com.Qapfoxo9.Diknos.core.di.ServiceLocator
import com.Qapfoxo9.Diknos.core.navigation.Screen
import com.Qapfoxo9.Diknos.core.theme.AccentStripe
import com.Qapfoxo9.Diknos.core.theme.BgBase
import com.Qapfoxo9.Diknos.core.theme.BgDeep
import com.Qapfoxo9.Diknos.core.theme.ScrimDim
import com.Qapfoxo9.Diknos.core.theme.SurfaceRaised
import com.Qapfoxo9.Diknos.core.theme.TextPrimary
import com.Qapfoxo9.Diknos.core.theme.TextSecondary
import com.Qapfoxo9.Diknos.core.theme.TopHighlight
import com.Qapfoxo9.Diknos.core.ui.AppBackground
import com.Qapfoxo9.Diknos.presentation.dialog.LeaveTrailDialog
import com.Qapfoxo9.Diknos.presentation.dialog.PauseDialog
import kotlin.math.floor
import kotlin.math.min

private const val BOARD_GAP_DP = 4f
private const val BOARD_FRAME_DP = 8f
private const val CONTROL_SPACE_DP = 150f

@Composable
fun GameRoute(
    levelIndex: Int,
    roundId: Int,
    onLeave: () -> Unit,
    onFinished: (Screen.Result) -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(factory = ServiceLocator.factory)
    val state by gameViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(roundId) {
        gameViewModel.prepareRound(levelIndex, roundId)
    }
    LaunchedEffect(Unit) {
        gameViewModel.roundFinished.collect { result -> onFinished(result) }
    }
    DisposableEffect(Unit) {
        onDispose { gameViewModel.pause() }
    }
    GameScreen(
        state = state,
        onTileTap = { index -> gameViewModel.onTileTap(index) },
        onToggleMark = { gameViewModel.toggleMarkMode() },
        onUseSense = { gameViewModel.useTigerSense() },
        onRequestLeave = { gameViewModel.requestLeave() },
        onDismissLeave = { gameViewModel.dismissLeave() },
        onConfirmLeave = {
            gameViewModel.abandonRound()
            onLeave()
        },
        onPause = { gameViewModel.openPause() },
        onResume = { gameViewModel.closePause() },
        onPauseToMenu = {
            gameViewModel.abandonRound()
            onLeave()
        }
    )
}

@Composable
fun GameScreen(
    state: GameUiState,
    onTileTap: (Int) -> Unit,
    onToggleMark: () -> Unit,
    onUseSense: () -> Unit,
    onRequestLeave: () -> Unit,
    onDismissLeave: () -> Unit,
    onConfirmLeave: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onPauseToMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BgBase)
    ) {
        AppBackground(
            overlay = listOf(
                BgBase.copy(alpha = 0.68f),
                BgDeep.copy(alpha = 0.86f),
                BgDeep.copy(alpha = 0.94f)
            )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            GameHeader(
                levelLine = stringResource(
                    R.string.menu_level_line,
                    state.levelIndex,
                    stringResource(state.regionRes)
                ),
                progress = state.pawprintProgress,
                progressLabel = stringResource(
                    R.string.pawprint_progress,
                    state.found,
                    state.totalPawprints
                ),
                onLeave = onRequestLeave,
                onPause = onPause
            )
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                val boardMaxWidth = (maxWidth - 32.dp).coerceAtMost(380.dp)
                val gaps = BOARD_GAP_DP * (GameConfig.COLS - 1)
                val tileByWidth =
                    floor((boardMaxWidth.value - 2f * BOARD_FRAME_DP - gaps) / GameConfig.COLS)
                val verticalGaps = BOARD_GAP_DP * (GameConfig.ROWS - 1)
                val tileByHeight = floor(
                    (maxHeight.value - CONTROL_SPACE_DP - 2f * BOARD_FRAME_DP - verticalGaps) /
                        GameConfig.ROWS
                )
                val tileDp = min(tileByWidth, tileByHeight).coerceAtLeast(26f)
                val boardWidth =
                    (tileDp * GameConfig.COLS + gaps + 2f * BOARD_FRAME_DP).dp
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(
                            if (state.markMode) R.string.game_hint_mark else R.string.game_hint
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (state.markMode) AccentStripe else TextSecondary,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .width(boardWidth)
                            .padding(bottom = 10.dp)
                    )
                    if (!state.board.isEmpty) {
                        TrekBoard(
                            board = state.board,
                            tileSize = tileDp.dp,
                            gap = BOARD_GAP_DP.dp,
                            enabled = !state.inputLocked && !state.paused,
                            onTap = onTileTap,
                            modifier = Modifier.width(boardWidth)
                        )
                    }
                }
                FloatingControlBar(
                    treksLeft = state.treksLeft,
                    lowOnTreks = state.lowOnTreks,
                    markMode = state.markMode,
                    senseCharges = state.senseCharges,
                    enabled = !state.inputLocked && !state.paused,
                    onToggleMark = onToggleMark,
                    onUseSense = onUseSense,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .width(boardWidth)
                        .padding(bottom = 24.dp)
                )
            }
        }
        if (state.paused) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ScrimDim)
            )
            PauseDialog(
                onResume = onResume,
                onMenu = onPauseToMenu
            )
        }
        if (state.leaveRequested) {
            LeaveTrailDialog(
                onLeave = onConfirmLeave,
                onStay = onDismissLeave
            )
        }
    }
}

@Composable
private fun GameHeader(
    levelLine: String,
    progress: Float,
    progressLabel: String,
    onLeave: () -> Unit,
    onPause: () -> Unit
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 300),
        label = "pawprintBar"
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(BgDeep.copy(alpha = 0.8f))
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onLeave,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.icon_home),
                contentDescription = stringResource(R.string.cd_home),
                tint = Color.Unspecified,
                modifier = Modifier.size(28.dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = levelLine,
                style = MaterialTheme.typography.titleSmall,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(SurfaceRaised)
                    .semantics {
                        liveRegion = LiveRegionMode.Polite
                        contentDescription = progressLabel
                    }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress.coerceIn(0f, 1f))
                        .height(4.dp)
                        .background(AccentStripe)
                )
            }
        }
        IconButton(
            onClick = onPause,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.icon_pause),
                contentDescription = stringResource(R.string.cd_pause),
                tint = Color.Unspecified,
                modifier = Modifier.size(28.dp)
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(TopHighlight)
    )
}
