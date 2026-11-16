package com.Qapfoxo9.Diknos.domain.model

enum class TileKind { CLEAR, PAWPRINT, NEST }

data class Tile(
    val kind: TileKind = TileKind.CLEAR,
    val sign: Int = 0,
    val revealed: Boolean = false,
    val marked: Boolean = false,
    val revealDelayMs: Int = 0
)
