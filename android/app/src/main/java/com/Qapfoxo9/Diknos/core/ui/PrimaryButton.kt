package com.Qapfoxo9.Diknos.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Qapfoxo9.Diknos.core.config.GameConfig
import com.Qapfoxo9.Diknos.core.theme.AccentStripe
import com.Qapfoxo9.Diknos.core.theme.AccentStripeEnd
import com.Qapfoxo9.Diknos.core.theme.BgBase
import com.Qapfoxo9.Diknos.core.theme.SurfaceRaised
import com.Qapfoxo9.Diknos.core.theme.TextMuted

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconRes: Int? = null,
    height: Dp = 58.dp,
    corner: Dp = 20.dp
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.96f else 1f,
        animationSpec = spring(
            dampingRatio = GameConfig.PRESS_DAMPING,
            stiffness = GameConfig.PRESS_STIFFNESS
        ),
        label = "primaryPress"
    )
    val shape = RoundedCornerShape(corner)
    val fill = if (enabled) {
        Brush.linearGradient(listOf(AccentStripe, AccentStripeEnd))
    } else {
        Brush.linearGradient(listOf(SurfaceRaised, SurfaceRaised))
    }
    Button(
        onClick = onClick,
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .height(height)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (pressed) 6.dp else 16.dp,
                shape = shape,
                ambientColor = AccentStripe,
                spotColor = AccentStripe
            )
            .background(fill, shape),
        enabled = enabled,
        shape = shape,
        interactionSource = interactionSource,
        contentPadding = PaddingValues(horizontal = 18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = BgBase,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = TextMuted
        )
    ) {
        if (iconRes != null) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
