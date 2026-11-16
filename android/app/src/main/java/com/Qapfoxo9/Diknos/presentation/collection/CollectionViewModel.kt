package com.Qapfoxo9.Diknos.presentation.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Qapfoxo9.Diknos.core.config.GameConfig
import com.Qapfoxo9.Diknos.data.sample.SampleData
import com.Qapfoxo9.Diknos.domain.model.JournalPage
import com.Qapfoxo9.Diknos.domain.model.JournalState
import com.Qapfoxo9.Diknos.domain.repository.ProgressRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CollectionViewModel(
    progress: ProgressRepository
) : ViewModel() {

    private val selectedIndex = MutableStateFlow(0)
    private val lockedMessage = MutableStateFlow(0)
    private var messageJob: Job? = null

    val state: StateFlow<CollectionUiState> =
        combine(progress.snapshot, selectedIndex, lockedMessage) { trek, selection, message ->
            val pages = SampleData.levels.map { level ->
                val stored = trek.journal.getOrElse(level.index - 1) { GameConfig.JOURNAL_LOCKED }
                JournalPage(
                    index = level.index,
                    titleRes = SampleData.journalTitles[level.index - 1],
                    regionRes = level.regionRes,
                    logRes = SampleData.journalLogs[level.index - 1],
                    state = when (stored) {
                        GameConfig.JOURNAL_GOLDEN -> JournalState.GOLDEN
                        GameConfig.JOURNAL_RECOVERED -> JournalState.RECOVERED
                        else -> JournalState.LOCKED
                    }
                )
            }
            CollectionUiState(
                pages = pages,
                recovered = pages.count { it.state != JournalState.LOCKED },
                selected = pages.firstOrNull {
                    it.index == selection && it.state != JournalState.LOCKED
                },
                lockedMessageLevel = message
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CollectionUiState()
        )

    fun openPage(index: Int) {
        selectedIndex.value = index
    }

    fun closePage() {
        selectedIndex.value = 0
    }

    fun onLockedTap(pageIndex: Int) {
        messageJob?.cancel()
        lockedMessage.value = pageIndex
        messageJob = viewModelScope.launch {
            delay(GameConfig.MESSAGE_DURATION_MS)
            lockedMessage.value = 0
        }
    }
}
