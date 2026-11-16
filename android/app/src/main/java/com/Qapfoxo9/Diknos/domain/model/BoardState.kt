package com.Qapfoxo9.Diknos.domain.model

data class BoardState(
    val tiles: List<Tile> = emptyList(),
    val cols: Int = 0,
    val rows: Int = 0,
    val seed: Long = 0L
) {
    val isEmpty: Boolean get() = tiles.isEmpty()

    val pawprintTotal: Int get() = tiles.count { it.kind == TileKind.PAWPRINT }

    val pawprintsFound: Int get() = tiles.count { it.kind == TileKind.PAWPRINT && it.revealed }

    fun neighbours(index: Int): List<Int> {
        val row = index / cols
        val col = index % cols
        val result = ArrayList<Int>(8)
        for (dr in -1..1) {
            for (dc in -1..1) {
                if (dr == 0 && dc == 0) continue
                val r = row + dr
                val c = col + dc
                if (r in 0 until rows && c in 0 until cols) result.add(r * cols + c)
            }
        }
        return result
    }

    fun withTiles(updated: List<Tile>): BoardState = copy(tiles = updated)

    fun revealAllNests(): BoardState = withTiles(
        tiles.map { if (it.kind == TileKind.NEST) it.copy(revealed = true, marked = false) else it }
    )
}
