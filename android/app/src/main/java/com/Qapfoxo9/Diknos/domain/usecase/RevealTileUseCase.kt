package com.Qapfoxo9.Diknos.domain.usecase

import com.Qapfoxo9.Diknos.core.config.GameConfig
import com.Qapfoxo9.Diknos.domain.model.BoardState
import com.Qapfoxo9.Diknos.domain.model.TileKind

class RevealTileUseCase {

    operator fun invoke(board: BoardState, index: Int): BoardState {
        val tile = board.tiles[index]
        if (tile.revealed || tile.marked) return board
        val updated = board.tiles.toMutableList()
        if (tile.kind == TileKind.NEST || tile.sign != 0) {
            updated[index] = tile.copy(revealed = true, revealDelayMs = 0)
            return board.withTiles(updated)
        }
        val wave = LinkedHashMap<Int, Int>()
        val queue = ArrayDeque<Int>()
        wave[index] = 0
        queue.add(index)
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (board.tiles[current].sign != 0) continue
            for (neighbour in board.neighbours(current)) {
                val candidate = board.tiles[neighbour]
                if (candidate.revealed || candidate.marked || wave.containsKey(neighbour)) continue
                wave[neighbour] = wave.size * GameConfig.FLOOD_STAGGER_MS
                queue.add(neighbour)
            }
        }
        for (entry in wave) {
            updated[entry.key] = updated[entry.key].copy(
                revealed = true,
                revealDelayMs = entry.value
            )
        }
        return board.withTiles(updated)
    }
}
