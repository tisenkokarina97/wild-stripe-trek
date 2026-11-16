package com.Qapfoxo9.Diknos.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Qapfoxo9.Diknos.core.config.GameConfig
import com.Qapfoxo9.Diknos.domain.model.DefaultTap
import com.Qapfoxo9.Diknos.domain.repository.ProgressRepository
import com.Qapfoxo9.Diknos.domain.repository.SettingsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val progress: ProgressRepository,
    private val settings: SettingsRepository
) : ViewModel() {

    private val resetDialog = MutableStateFlow(false)
    private val erasedMessage = MutableStateFlow(false)
    private var messageJob: Job? = null

    val state: StateFlow<SettingsUiState> =
        combine(settings.snapshot, resetDialog, erasedMessage) { options, dialog, erased ->
            SettingsUiState(
                defaultTap = options.defaultTap,
                safeFirstStep = options.safeFirstStep,
                resetDialogVisible = dialog,
                erasedMessageVisible = erased
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsUiState()
        )

    fun selectDefaultTap(tap: DefaultTap) {
        settings.setDefaultTap(tap)
    }

    fun setSafeFirstStep(enabled: Boolean) {
        settings.setSafeFirstStep(enabled)
    }

    fun requestReset() {
        resetDialog.value = true
    }

    fun cancelReset() {
        resetDialog.value = false
    }

    fun confirmReset() {
        progress.reset()
        resetDialog.value = false
        messageJob?.cancel()
        erasedMessage.value = true
        messageJob = viewModelScope.launch {
            delay(GameConfig.MESSAGE_DURATION_MS)
            erasedMessage.value = false
        }
    }
}
