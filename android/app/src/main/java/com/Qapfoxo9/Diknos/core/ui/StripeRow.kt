package com.Qapfoxo9.Diknos.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Qapfoxo9.Diknos.core.theme.AccentStripe
import com.Qapfoxo9.Diknos.core.theme.SurfaceMuted

@Composable
fun StripeRow(
    earned: Int,
    modifier: Modifier = Modifier,
    total: Int = 3,
    barWidth: Dp = 26.dp,
    barHeight: Dp = 6.dp,
    gap: Dp = 4.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(gap)
    ) {
        for (index in 0 until total) {
            Spacer(
                modifier = Modifier
                    .size(width = barWidth, height = barHeight)
                    .clip(RoundedCornerShape(barHeight / 2))
                    .background(if (index < earned) AccentStripe else SurfaceMuted)
            )
        }
    }
}
