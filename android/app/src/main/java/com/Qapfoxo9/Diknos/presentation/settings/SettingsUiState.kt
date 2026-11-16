package com.Qapfoxo9.Diknos.presentation.settings

import com.Qapfoxo9.Diknos.domain.model.DefaultTap

data class SettingsUiState(
    val defaultTap: DefaultTap = DefaultTap.REVEAL,
    val safeFirstStep: Boolean = true,
    val resetDialogVisible: Boolean = false,
    val erasedMessageVisible: Boolean = false
)
