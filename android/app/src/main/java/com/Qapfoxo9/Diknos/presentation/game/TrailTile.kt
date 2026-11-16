package com.Qapfoxo9.Diknos.presentation.game

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Qapfoxo9.Diknos.R
import com.Qapfoxo9.Diknos.core.config.GameConfig
import com.Qapfoxo9.Diknos.core.theme.AccentStripe
import com.Qapfoxo9.Diknos.core.theme.Danger
import com.Qapfoxo9.Diknos.core.theme.ShadowTint
import com.Qapfoxo9.Diknos.core.theme.SurfaceCleared
import com.Qapfoxo9.Diknos.core.theme.SurfaceRaised
import com.Qapfoxo9.Diknos.core.theme.TileTop
import com.Qapfoxo9.Diknos.core.theme.TopHighlight
import com.Qapfoxo9.Diknos.core.theme.trailSignColor
import com.Qapfoxo9.Diknos.domain.model.Tile
import com.Qapfoxo9.Diknos.domain.model.TileKind

@Composable
fun TrailTile(
    tile: Tile,
    size: Dp,
    enabled: Boolean,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val flip by animateFloatAsState(
        targetValue = if (tile.revealed) 1f else 0f,
        animationSpec = tween(
            durationMillis = GameConfig.TILE_FLIP_MS,
            delayMillis = tile.revealDelayMs,
            easing = FastOutSlowInEasing
        ),
        label = "tileFlip"
    )
    val opened = flip >= 0.5f
    val rotation = if (flip < 0.5f) flip * 180f else (flip - 1f) * 180f
    val shape = RoundedCornerShape(12.dp)
    val description = when {
        tile.revealed -> stringResource(R.string.sd_revealed)
        tile.marked -> stringResource(R.string.sd_flagged)
        else -> stringResource(R.string.sd_hidden)
    }
    Box(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                rotationX = rotation
                cameraDistance = 16f * density
            }
            .shadow(elevation = if (opened) 0.dp else 6.dp, shape = shape, spotColor = ShadowTint)
            .clip(shape)
            .clickable(enabled = enabled, onClick = onTap)
            .semantics { stateDescription = description },
        contentAlignment = Alignment.Center
    ) {
        if (opened) {
            OpenedFace(tile = tile, size = size, shape = shape)
        } else {
            HiddenFace(tile = tile, size = size, shape = shape)
        }
    }
}

@Composable
private fun HiddenFace(tile: Tile, size: Dp, shape: RoundedCornerShape) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(SurfaceRaised, TileTop)), shape)
            .border(
                width = if (tile.marked) 1.5.dp else 1.dp,
                color = if (tile.marked) AccentStripe else TopHighlight,
                shape = shape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (tile.marked) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AccentStripe.copy(alpha = 0.15f), shape)
            )
            Image(
                painter = painterResource(R.drawable.sprite_5),
                contentDescription = stringResource(R.string.cd_flag),
                modifier = Modifier.size(size * 0.6f),
                contentScale = ContentScale.Fit
            )
        } else {
            LeafChevron()
        }
    }
}

@Composable
private fun OpenedFace(tile: Tile, size: Dp, shape: RoundedCornerShape) {
    val fill = when (tile.kind) {
        TileKind.NEST -> Danger.copy(alpha = 0.15f)
        TileKind.PAWPRINT -> AccentStripe.copy(alpha = 0.13f)
        TileKind.CLEAR -> SurfaceCleared
    }
    val borderColor = when (tile.kind) {
        TileKind.NEST -> Danger
        TileKind.PAWPRINT -> AccentStripe
        TileKind.CLEAR -> Color.Transparent
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fill, shape)
            .border(width = 1.5.dp, color = borderColor, shape = shape),
        contentAlignment = Alignment.Center
    ) {
        when (tile.kind) {
            TileKind.NEST -> Image(
                painter = painterResource(R.drawable.sprite_3),
                contentDescription = stringResource(R.string.cd_nest),
                modifier = Modifier.size(size * 0.7f),
                contentScale = ContentScale.Fit
            )

            TileKind.PAWPRINT -> {
                Image(
                    painter = painterResource(R.drawable.sprite_2),
                    contentDescription = stringResource(R.string.cd_pawprint),
                    modifier = Modifier.size(size * 0.68f),
                    contentScale = ContentScale.Fit
                )
                if (tile.sign > 0) {
                    Text(
                        text = tile.sign.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = trailSignColor(tile.sign),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 5.dp, bottom = 3.dp)
                    )
                }
            }

            TileKind.CLEAR -> if (tile.sign > 0) {
                Text(
                    text = tile.sign.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = trailSignColor(tile.sign)
                )
            }
        }
    }
}

@Composable
private fun LeafChevron(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        drawLine(
            color = TopHighlight,
            start = Offset(width * 0.3f, height * 0.62f),
            end = Offset(width * 0.5f, height * 0.38f),
            strokeWidth = 3f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = TopHighlight,
            start = Offset(width * 0.5f, height * 0.38f),
            end = Offset(width * 0.7f, height * 0.62f),
            strokeWidth = 3f,
            cap = StrokeCap.Round
        )
    }
}
