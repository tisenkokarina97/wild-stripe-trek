package com.Qapfoxo9.Diknos.presentation.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Qapfoxo9.Diknos.core.theme.AccentStripe
import com.Qapfoxo9.Diknos.core.theme.SurfaceSunken
import com.Qapfoxo9.Diknos.core.ui.ClayCard
import com.Qapfoxo9.Diknos.domain.model.BoardState

@Composable
fun TrekBoard(
    board: BoardState,
    tileSize: Dp,
    gap: Dp,
    enabled: Boolean,
    onTap: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    ClayCard(
        modifier = modifier,
        fill = SurfaceSunken,
        border = BorderStroke(2.dp, AccentStripe.copy(alpha = 0.2f)),
        corner = 26.dp,
        elevation = 14.dp
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.spacedBy(gap)
        ) {
            for (row in 0 until board.rows) {
                Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
                    for (col in 0 until board.cols) {
                        val index = row * board.cols + col
                        TrailTile(
                            tile = board.tiles[index],
                            size = tileSize,
                            enabled = enabled && !board.tiles[index].revealed,
                            onTap = { onTap(index) }
                        )
                    }
                }
            }
        }
    }
}
