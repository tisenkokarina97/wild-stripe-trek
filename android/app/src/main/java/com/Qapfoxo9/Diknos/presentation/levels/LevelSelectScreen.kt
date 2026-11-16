package com.Qapfoxo9.Diknos.presentation.levels

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Qapfoxo9.Diknos.R
import com.Qapfoxo9.Diknos.core.di.ServiceLocator
import com.Qapfoxo9.Diknos.core.theme.AccentTrail
import com.Qapfoxo9.Diknos.core.theme.BgBase
import com.Qapfoxo9.Diknos.core.theme.BgDeep
import com.Qapfoxo9.Diknos.core.theme.Gold
import com.Qapfoxo9.Diknos.core.theme.Hairline
import com.Qapfoxo9.Diknos.core.theme.SurfaceBase
import com.Qapfoxo9.Diknos.core.theme.SurfaceRaised
import com.Qapfoxo9.Diknos.core.theme.TextMuted
import com.Qapfoxo9.Diknos.core.theme.TextPrimary
import com.Qapfoxo9.Diknos.core.theme.TextSecondary
import com.Qapfoxo9.Diknos.core.ui.ScreenScaffold
import com.Qapfoxo9.Diknos.core.ui.StripeRow
import kotlin.math.floor

private const val GRID_COLS = 3
private const val GRID_ROWS = 4

@Composable
fun LevelSelectRoute(
    onBack: () -> Unit,
    onLevel: (Int) -> Unit
) {
    val levelsViewModel: LevelSelectViewModel = viewModel(factory = ServiceLocator.factory)
    val state by levelsViewModel.state.collectAsStateWithLifecycle()
    LevelSelectScreen(
        state = state,
        onBack = onBack,
        onLevel = onLevel,
        onLocked = { required -> levelsViewModel.onLockedTap(required) }
    )
}

@Composable
fun LevelSelectScreen(
    state: LevelSelectUiState,
    onBack: () -> Unit,
    onLevel: (Int) -> Unit,
    onLocked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    ScreenScaffold(
        titleRes = R.string.level_select,
        onBack = onBack,
        overlay = listOf(
            BgBase.copy(alpha = 0.62f),
            BgBase.copy(alpha = 0.78f),
            BgDeep.copy(alpha = 0.90f)
        ),
        modifier = modifier,
        trailing = {
            Text(
                text = stringResource(
                    R.string.fraction,
                    state.stripesEarned,
                    state.stripesTotal
                ),
                style = MaterialTheme.typography.labelLarge,
                color = Gold,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    ) {
        if (state.lockedMessageLevel > 0) {
            Text(
                text = stringResource(R.string.level_locked_message, state.lockedMessageLevel),
                style = MaterialTheme.typography.labelSmall,
                color = Gold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            val available = maxWidth
            val tile: Dp = floor(
                (available.value - GAP_DP * (GRID_COLS - 1)) / GRID_COLS
            ).coerceAtMost(TILE_MAX_DP).dp
            Column(
                modifier = Modifier.align(Alignment.TopCenter),
                verticalArrangement = Arrangement.spacedBy(GAP_DP.dp)
            ) {
                for (row in 0 until GRID_ROWS) {
                    Row(horizontalArrangement = Arrangement.spacedBy(GAP_DP.dp)) {
                        for (col in 0 until GRID_COLS) {
                            val position = row * GRID_COLS + col
                            val item = state.tiles.getOrNull(position)
                            if (item != null) {
                                LevelTile(
                                    item = item,
                                    size = tile,
                                    onPlay = { onLevel(item.index) },
                                    onLocked = { onLocked(item.index - 1) }
                                )
                            } else {
                                Spacer(modifier = Modifier.size(tile))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LevelTile(
    item: LevelTileUi,
    size: Dp,
    onPlay: () -> Unit,
    onLocked: () -> Unit
) {
    val transition = rememberInfiniteTransition(label = "currentLevel")
    val glow by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "currentGlow"
    )
    val shape = RoundedCornerShape(20.dp)
    val borderColor = when {
        item.isCurrent -> AccentTrail.copy(alpha = glow)
        item.unlocked -> Hairline
        else -> Color.Transparent
    }
    val fill = if (item.unlocked) SurfaceRaised else SurfaceBase.copy(alpha = 0.6f)
    val lockedLabel = stringResource(R.string.sd_locked)
    Button(
        onClick = { if (item.unlocked) onPlay() else onLocked() },
        modifier = Modifier
            .size(size)
            .shadow(elevation = if (item.unlocked) 10.dp else 2.dp, shape = shape)
            .background(fill, shape)
            .semantics {
                if (!item.unlocked) stateDescription = lockedLabel
            },
        shape = shape,
        border = BorderStroke(if (item.isCurrent) 2.dp else 1.5.dp, borderColor),
        contentPadding = PaddingValues(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = TextPrimary
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (item.unlocked) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.level_number, item.index),
                        style = MaterialTheme.typography.headlineSmall,
                        color = TextPrimary
                    )
                    Text(
                        text = stringResource(item.regionRes),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 2.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    StripeRow(
                        earned = item.stripes,
                        barWidth = 10.dp,
                        barHeight = 3.dp,
                        gap = 3.dp
                    )
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_lock),
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.locked),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                }
            }
        }
    }
}

private const val GAP_DP = 12f
private const val TILE_MAX_DP = 108f
