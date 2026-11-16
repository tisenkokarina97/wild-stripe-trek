package com.Qapfoxo9.Diknos.presentation.levels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Qapfoxo9.Diknos.core.config.GameConfig
import com.Qapfoxo9.Diknos.data.sample.SampleData
import com.Qapfoxo9.Diknos.domain.repository.ProgressRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LevelSelectViewModel(
    progress: ProgressRepository
) : ViewModel() {

    private val lockedMessage = MutableStateFlow(0)
    private var messageJob: Job? = null

    val state: StateFlow<LevelSelectUiState> =
        combine(progress.snapshot, lockedMessage) { trek, message ->
            LevelSelectUiState(
                tiles = SampleData.levels.map { level ->
                    LevelTileUi(
                        index = level.index,
                        regionRes = level.regionRes,
                        unlocked = level.index <= trek.unlockedLevel,
                        stripes = trek.stripes.getOrElse(level.index - 1) { 0 },
                        isCurrent = level.index == trek.unlockedLevel
                    )
                },
                stripesEarned = trek.stripesEarned,
                stripesTotal = GameConfig.LEVEL_COUNT * 3,
                lockedMessageLevel = message
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LevelSelectUiState()
        )

    fun onLockedTap(requiredLevel: Int) {
        messageJob?.cancel()
        lockedMessage.value = requiredLevel
        messageJob = viewModelScope.launch {
            delay(GameConfig.MESSAGE_DURATION_MS)
            lockedMessage.value = 0
        }
    }
}
