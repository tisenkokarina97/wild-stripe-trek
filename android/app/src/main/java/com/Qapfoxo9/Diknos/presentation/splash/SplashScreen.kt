package com.Qapfoxo9.Diknos.presentation.splash

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Qapfoxo9.Diknos.R
import com.Qapfoxo9.Diknos.core.di.ServiceLocator
import com.Qapfoxo9.Diknos.core.theme.AccentStripe
import com.Qapfoxo9.Diknos.core.theme.AccentTrail
import com.Qapfoxo9.Diknos.core.theme.BgBase
import com.Qapfoxo9.Diknos.core.theme.BgDeep
import com.Qapfoxo9.Diknos.core.theme.BgDeepest
import com.Qapfoxo9.Diknos.core.theme.SurfaceBase
import com.Qapfoxo9.Diknos.core.theme.SurfaceSunken
import com.Qapfoxo9.Diknos.core.theme.TextMuted
import com.Qapfoxo9.Diknos.core.theme.TextPrimary
import com.Qapfoxo9.Diknos.core.theme.TextSecondary
import com.Qapfoxo9.Diknos.core.ui.AppBackground
import com.Qapfoxo9.Diknos.core.ui.ClayCard
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SplashRoute(onFinished: () -> Unit) {
    val splashViewModel: SplashViewModel = viewModel(factory = ServiceLocator.factory)
    val finished by splashViewModel.finished.collectAsStateWithLifecycle()
    LaunchedEffect(finished) {
        if (finished) onFinished()
    }
    SplashScreen()
}

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        entered = true
    }
    val medallionScale by animateFloatAsState(
        targetValue = if (entered) 1f else 0.82f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 380f),
        label = "medallion"
    )
    val titleAlpha by animateFloatAsState(
        targetValue = if (entered) 1f else 0f,
        animationSpec = tween(durationMillis = 420),
        label = "titleFade"
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BgBase)
    ) {
        AppBackground(
            overlay = listOf(
                BgBase.copy(alpha = 0.94f),
                BgDeep.copy(alpha = 0.98f),
                BgDeepest
            )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(196.dp),
                contentAlignment = Alignment.Center
            ) {
                StripeRing()
                ClayCard(
                    modifier = Modifier
                        .size(148.dp)
                        .graphicsLayer {
                            scaleX = medallionScale
                            scaleY = medallionScale
                        },
                    fill = SurfaceBase,
                    border = BorderStroke(2.dp, AccentStripe.copy(alpha = 0.34f)),
                    corner = 36.dp,
                    elevation = 18.dp,
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = stringResource(R.string.cd_logo),
                        modifier = Modifier.size(96.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
            Spacer(modifier = Modifier.height(36.dp))
            Text(
                text = stringResource(R.string.title_app),
                style = MaterialTheme.typography.displaySmall.copy(
                    shadow = Shadow(
                        color = AccentStripe.copy(alpha = 0.4f),
                        offset = Offset(0f, 4f),
                        blurRadius = 16f
                    )
                ),
                color = TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.graphicsLayer { alpha = titleAlpha }
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.splash_tagline),
                style = MaterialTheme.typography.labelLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.graphicsLayer { alpha = titleAlpha }
            )
            Spacer(modifier = Modifier.height(44.dp))
            LinearProgressIndicator(
                modifier = Modifier
                    .width(196.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = AccentTrail,
                trackColor = SurfaceSunken
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.loading),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }
    }
}

@Composable
private fun StripeRing(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "ring")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 14000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ringAngle"
    )
    Canvas(modifier = modifier.size(196.dp)) {
        val radius = size.minDimension / 2f - 8f
        val center = Offset(size.width / 2f, size.height / 2f)
        for (index in 0 until 6) {
            val radians = ((angle + index * 60f) * kotlin.math.PI / 180f).toFloat()
            val inner = radius - 18f
            drawLine(
                color = AccentStripe.copy(alpha = 0.55f),
                start = Offset(
                    center.x + cos(radians) * inner,
                    center.y + sin(radians) * inner
                ),
                end = Offset(
                    center.x + cos(radians) * radius,
                    center.y + sin(radians) * radius
                ),
                strokeWidth = 7f,
                cap = StrokeCap.Round
            )
        }
    }
}
