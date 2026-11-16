package com.Qapfoxo9.Diknos.domain.model

object FallbackLayouts {

    private val NESTS: List<IntArray> = listOf(
        intArrayOf(27, 28, 29),
        intArrayOf(26, 27, 28, 29),
        intArrayOf(25, 26, 27, 28, 29),
        intArrayOf(24, 25, 26, 27, 28, 29),
        intArrayOf(24, 25, 26, 27, 28, 29),
        intArrayOf(23, 24, 25, 26, 27, 28, 29),
        intArrayOf(23, 24, 25, 26, 27, 28, 29),
        intArrayOf(22, 23, 24, 25, 26, 27, 28, 29),
        intArrayOf(22, 23, 24, 25, 26, 27, 28, 29),
        intArrayOf(21, 22, 23, 24, 25, 26, 27, 28, 29),
        intArrayOf(21, 22, 23, 24, 25, 26, 27, 28, 29),
        intArrayOf(20, 21, 22, 23, 24, 25, 26, 27, 28, 29)
    )

    private val PAWPRINTS: List<IntArray> = listOf(
        intArrayOf(0, 2, 4),
        intArrayOf(0, 2, 4),
        intArrayOf(0, 2, 4, 6),
        intArrayOf(0, 2, 4, 6),
        intArrayOf(0, 2, 4, 6, 8),
        intArrayOf(0, 2, 4, 6, 8),
        intArrayOf(0, 2, 4, 6, 8, 10),
        intArrayOf(0, 2, 4, 6, 8, 10),
        intArrayOf(0, 2, 4, 6, 8, 10, 12),
        intArrayOf(0, 2, 4, 6, 8, 10, 12),
        intArrayOf(0, 2, 4, 6, 8, 10, 12, 14),
        intArrayOf(0, 2, 4, 6, 8, 10, 12, 14)
    )

    fun nestsFor(levelIndex: Int): IntArray = NESTS[(levelIndex - 1).coerceIn(0, NESTS.size - 1)]

    fun pawprintsFor(levelIndex: Int): IntArray =
        PAWPRINTS[(levelIndex - 1).coerceIn(0, PAWPRINTS.size - 1)]
}
