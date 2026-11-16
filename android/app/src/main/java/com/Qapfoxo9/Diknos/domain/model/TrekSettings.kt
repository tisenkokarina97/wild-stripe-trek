package com.Qapfoxo9.Diknos.domain.model

enum class DefaultTap { REVEAL, MARK }

data class TrekSettings(
    val defaultTap: DefaultTap = DefaultTap.REVEAL,
    val safeFirstStep: Boolean = true
)
