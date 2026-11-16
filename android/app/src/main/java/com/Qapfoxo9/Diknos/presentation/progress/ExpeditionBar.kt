package com.Qapfoxo9.Diknos.presentation.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.Qapfoxo9.Diknos.core.config.GameConfig
import com.Qapfoxo9.Diknos.core.theme.AccentTrail
import com.Qapfoxo9.Diknos.core.theme.Gold
import com.Qapfoxo9.Diknos.core.theme.SurfaceRaised

@Composable
fun ExpeditionBar(
    segments: List<Int>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        for (index in 0 until GameConfig.LEVEL_COUNT) {
            val value = segments.getOrElse(index) { GameConfig.JOURNAL_LOCKED }
            val color = when (value) {
                GameConfig.JOURNAL_GOLDEN -> Gold
                GameConfig.JOURNAL_RECOVERED -> AccentTrail
                else -> SurfaceRaised
            }
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(color)
            )
        }
    }
}
