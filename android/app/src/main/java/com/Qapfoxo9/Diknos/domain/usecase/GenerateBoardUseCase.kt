package com.Qapfoxo9.Diknos.domain.usecase

import com.Qapfoxo9.Diknos.core.config.GameConfig
import com.Qapfoxo9.Diknos.domain.model.BoardState
import com.Qapfoxo9.Diknos.domain.model.FallbackLayouts
import com.Qapfoxo9.Diknos.domain.model.Tile
import com.Qapfoxo9.Diknos.domain.model.TileKind
import com.Qapfoxo9.Diknos.domain.model.TrekLevel
import kotlin.random.Random

class GenerateBoardUseCase {

    operator fun invoke(level: TrekLevel, attempt: Int, safeFirstStep: Boolean): BoardState {
        val seed = level.index.toLong() * 7919L xor (attempt.toLong() * 104729L)
        val random = Random(seed)
        repeat(GameConfig.GENERATION_ATTEMPTS) {
            val candidate = build(level, random, seed)
            if (isSolvable(candidate, safeFirstStep)) return candidate
        }
        return assemble(
            FallbackLayouts.nestsFor(level.index).toSet(),
            FallbackLayouts.pawprintsFor(level.index).toSet(),
            seed
        )
    }

    private fun build(level: TrekLevel, random: Random, seed: Long): BoardState {
        val positions = (0 until GameConfig.TILE_COUNT).shuffled(random)
        val nests = positions.take(level.nests).toSet()
        val pawprints = positions.drop(level.nests).take(level.pawprints).toSet()
        return assemble(nests, pawprints, seed)
    }

    private fun assemble(nests: Set<Int>, pawprints: Set<Int>, seed: Long): BoardState {
        val blank = BoardState(
            tiles = List(GameConfig.TILE_COUNT) { Tile() },
            cols = GameConfig.COLS,
            rows = GameConfig.ROWS,
            seed = seed
        )
        val tiles = (0 until GameConfig.TILE_COUNT).map { index ->
            val kind = when {
                nests.contains(index) -> TileKind.NEST
                pawprints.contains(index) -> TileKind.PAWPRINT
                else -> TileKind.CLEAR
            }
            Tile(kind = kind, sign = blank.neighbours(index).count { nests.contains(it) })
        }
        return blank.withTiles(tiles)
    }

    private fun isSolvable(board: BoardState, safeFirstStep: Boolean): Boolean {
        if (board.tiles.count { it.kind != TileKind.CLEAR } > GameConfig.MAX_OCCUPIED_TILES) return false
        val openings = board.tiles.indices.filter {
            board.tiles[it].kind == TileKind.CLEAR && board.tiles[it].sign == 0
        }
        if (openings.isEmpty()) return false
        return openings.any { origin ->
            val region = openRegion(board, origin)
            if (safeFirstStep && region.size < GameConfig.MIN_OPENING_REGION) {
                false
            } else {
                deduce(board, region)
            }
        }
    }

    private fun openRegion(board: BoardState, origin: Int): Set<Int> {
        val opened = LinkedHashSet<Int>()
        val queue = ArrayDeque<Int>()
        opened.add(origin)
        queue.add(origin)
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (board.tiles[current].sign != 0) continue
            for (neighbour in board.neighbours(current)) {
                if (opened.add(neighbour)) queue.add(neighbour)
            }
        }
        return opened
    }

    private fun deduce(board: BoardState, region: Set<Int>): Boolean {
        val safe = region.toMutableSet()
        val nests = mutableSetOf<Int>()
        propagate(board, safe, nests)
        if (allPawprintsFound(board, safe)) return true
        for (guess in board.tiles.indices) {
            if (safe.contains(guess) || nests.contains(guess)) continue
            if (board.tiles[guess].kind == TileKind.NEST) continue
            val guessedSafe = safe.toMutableSet()
            val guessedNests = nests.toMutableSet()
            guessedSafe.add(guess)
            propagate(board, guessedSafe, guessedNests)
            if (allPawprintsFound(board, guessedSafe)) return true
        }
        return false
    }

    private fun propagate(board: BoardState, safe: MutableSet<Int>, nests: MutableSet<Int>) {
        var changed = true
        while (changed) {
            changed = false
            for (index in safe.toList()) {
                val tile = board.tiles[index]
                val neighbours = board.neighbours(index)
                val hidden = neighbours.filter { !safe.contains(it) && !nests.contains(it) }
                if (hidden.isEmpty()) continue
                val flagged = neighbours.count { nests.contains(it) }
                if (tile.sign == flagged) {
                    safe.addAll(hidden)
                    changed = true
                } else if (tile.sign - flagged == hidden.size) {
                    nests.addAll(hidden)
                    changed = true
                }
            }
        }
    }

    private fun allPawprintsFound(board: BoardState, safe: Set<Int>): Boolean =
        board.tiles.indices.none {
            board.tiles[it].kind == TileKind.PAWPRINT && !safe.contains(it)
        }
}
