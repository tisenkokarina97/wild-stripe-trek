package com.Qapfoxo9.Diknos.presentation.game

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Qapfoxo9.Diknos.R
import com.Qapfoxo9.Diknos.core.theme.AccentStripe
import com.Qapfoxo9.Diknos.core.theme.AccentTrail
import com.Qapfoxo9.Diknos.core.theme.Danger
import com.Qapfoxo9.Diknos.core.theme.Hairline
import com.Qapfoxo9.Diknos.core.theme.SurfaceBase
import com.Qapfoxo9.Diknos.core.theme.TextPrimary
import com.Qapfoxo9.Diknos.core.theme.TextSecondary
import com.Qapfoxo9.Diknos.core.ui.ClayCard
import com.Qapfoxo9.Diknos.core.ui.PrimaryButton
import com.Qapfoxo9.Diknos.core.ui.SecondaryButton

@Composable
fun FloatingControlBar(
    treksLeft: Int,
    lowOnTreks: Boolean,
    markMode: Boolean,
    senseCharges: Int,
    enabled: Boolean,
    onToggleMark: () -> Unit,
    onUseSense: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "treksPulse")
    val pulse by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 620),
            repeatMode = RepeatMode.Reverse
        ),
        label = "treksScale"
    )
    val trekScale = if (lowOnTreks) pulse else 1f
    val trekDescription = stringResource(R.string.trek_progress, treksLeft)
    val markState = stringResource(if (markMode) R.string.sd_mark_on else R.string.sd_mark_off)
    ClayCard(
        modifier = modifier.height(72.dp),
        fill = SurfaceBase,
        border = BorderStroke(1.5.dp, Hairline),
        corner = 26.dp,
        elevation = 14.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .semantics {
                        liveRegion = LiveRegionMode.Polite
                        contentDescription = trekDescription
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_1),
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box {
                    Text(
                        text = treksLeft.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = if (lowOnTreks) Danger else TextPrimary,
                        modifier = Modifier.graphicsLayer {
                            scaleX = trekScale
                            scaleY = trekScale
                        }
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.treks),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }
            if (markMode) {
                PrimaryButton(
                    text = stringResource(R.string.mark),
                    onClick = onToggleMark,
                    enabled = enabled,
                    height = 50.dp,
                    corner = 18.dp,
                    modifier = Modifier
                        .weight(1f)
                        .semantics { stateDescription = markState }
                )
            } else {
                SecondaryButton(
                    text = stringResource(R.string.mark),
                    onClick = onToggleMark,
                    enabled = enabled,
                    accent = AccentStripe,
                    modifier = Modifier
                        .weight(1f)
                        .semantics { stateDescription = markState }
                )
            }
            SecondaryButton(
                text = stringResource(R.string.sense_label, senseCharges),
                onClick = onUseSense,
                enabled = enabled && senseCharges > 0,
                accent = AccentTrail,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
