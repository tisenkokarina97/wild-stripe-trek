package com.Qapfoxo9.Diknos.presentation.levels

import com.Qapfoxo9.Diknos.core.config.GameConfig

data class LevelTileUi(
    val index: Int,
    val regionRes: Int,
    val unlocked: Boolean,
    val stripes: Int,
    val isCurrent: Boolean
)

data class LevelSelectUiState(
    val tiles: List<LevelTileUi> = emptyList(),
    val stripesEarned: Int = 0,
    val stripesTotal: Int = GameConfig.LEVEL_COUNT * 3,
    val lockedMessageLevel: Int = 0
)
