package com.Qapfoxo9.Diknos.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Qapfoxo9.Diknos.core.theme.Hairline
import com.Qapfoxo9.Diknos.core.theme.ShadowTint
import com.Qapfoxo9.Diknos.core.theme.SurfaceRaised
import com.Qapfoxo9.Diknos.core.theme.TopHighlight

@Composable
fun ClayCard(
    modifier: Modifier = Modifier,
    fill: Color = SurfaceRaised,
    gradient: Brush? = null,
    border: BorderStroke = BorderStroke(1.5.dp, Hairline),
    corner: Dp = 24.dp,
    elevation: Dp = 10.dp,
    shadowColor: Color = ShadowTint,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(corner)
    Box(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = shadowColor,
                spotColor = shadowColor
            )
            .clip(shape)
            .then(if (gradient == null) Modifier.background(fill) else Modifier.background(gradient))
            .border(border, shape)
            .drawWithContent {
                drawContent()
                val inset = corner.toPx() * 0.55f
                drawLine(
                    color = TopHighlight,
                    start = Offset(inset, 1.5f),
                    end = Offset(size.width - inset, 1.5f),
                    strokeWidth = 2f
                )
            },
        contentAlignment = contentAlignment,
        content = content
    )
}
