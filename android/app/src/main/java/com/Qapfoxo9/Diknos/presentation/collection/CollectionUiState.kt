package com.Qapfoxo9.Diknos.presentation.collection

import com.Qapfoxo9.Diknos.domain.model.JournalPage

data class CollectionUiState(
    val pages: List<JournalPage> = emptyList(),
    val recovered: Int = 0,
    val selected: JournalPage? = null,
    val lockedMessageLevel: Int = 0
)
