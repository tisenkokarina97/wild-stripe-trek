package com.Qapfoxo9.Diknos.core.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.Qapfoxo9.Diknos.R
import com.Qapfoxo9.Diknos.core.theme.AccentStripe
import com.Qapfoxo9.Diknos.core.theme.AccentTrail
import com.Qapfoxo9.Diknos.core.theme.ClawSlash

@Composable
fun AppBackground(
    overlay: List<Color>,
    modifier: Modifier = Modifier,
    showBlobs: Boolean = true,
    showClaws: Boolean = false
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(overlay))
        )
        if (showBlobs) {
            DriftingBlobs()
        }
        if (showClaws) {
            ClawSlashes()
        }
    }
}

@Composable
fun DriftingBlobs(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "blobs")
    val first by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blobOne"
    )
    val second by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 7400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blobTwo"
    )
    Canvas(modifier = modifier.fillMaxSize()) {
        val trailCenter = Offset(size.width * 0.24f, size.height * (0.20f + 0.05f * first))
        val trailRadius = size.minDimension * 0.52f
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(AccentTrail.copy(alpha = 0.12f), Color.Transparent),
                center = trailCenter,
                radius = trailRadius
            ),
            radius = trailRadius,
            center = trailCenter
        )
        val stripeCenter = Offset(size.width * (0.80f - 0.06f * second), size.height * 0.68f)
        val stripeRadius = size.minDimension * 0.58f
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(AccentStripe.copy(alpha = 0.09f), Color.Transparent),
                center = stripeCenter,
                radius = stripeRadius
            ),
            radius = stripeRadius,
            center = stripeCenter
        )
    }
}

@Composable
fun ClawSlashes(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val base = size.height * 0.84f
        for (index in 0 until 3) {
            val shift = index * (size.width * 0.07f)
            drawLine(
                color = ClawSlash,
                start = Offset(size.width * 0.58f + shift, base),
                end = Offset(size.width * 0.94f + shift, base - size.height * 0.12f),
                strokeWidth = 10f
            )
        }
    }
}
