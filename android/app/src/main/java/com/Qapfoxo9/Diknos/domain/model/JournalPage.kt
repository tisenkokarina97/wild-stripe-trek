package com.Qapfoxo9.Diknos.domain.model

enum class JournalState { LOCKED, RECOVERED, GOLDEN }

data class JournalPage(
    val index: Int,
    val titleRes: Int,
    val regionRes: Int,
    val logRes: Int,
    val state: JournalState
)
