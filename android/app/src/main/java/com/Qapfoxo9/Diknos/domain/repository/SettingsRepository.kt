package com.Qapfoxo9.Diknos.domain.repository

import com.Qapfoxo9.Diknos.domain.model.DefaultTap
import com.Qapfoxo9.Diknos.domain.model.TrekSettings
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {

    val snapshot: StateFlow<TrekSettings>

    fun setDefaultTap(tap: DefaultTap)

    fun setSafeFirstStep(enabled: Boolean)
}
