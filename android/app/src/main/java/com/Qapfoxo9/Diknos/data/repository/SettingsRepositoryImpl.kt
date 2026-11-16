package com.Qapfoxo9.Diknos.data.repository

import com.Qapfoxo9.Diknos.data.local.TrekPreferences
import com.Qapfoxo9.Diknos.domain.model.DefaultTap
import com.Qapfoxo9.Diknos.domain.model.TrekSettings
import com.Qapfoxo9.Diknos.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl(
    private val prefs: TrekPreferences
) : SettingsRepository {

    private val state = MutableStateFlow(read())

    override val snapshot: StateFlow<TrekSettings> = state.asStateFlow()

    override fun setDefaultTap(tap: DefaultTap) {
        prefs.writeInt(TrekPreferences.KEY_DEFAULT_TAP, tap.ordinal)
        state.value = read()
    }

    override fun setSafeFirstStep(enabled: Boolean) {
        prefs.writeBoolean(TrekPreferences.KEY_SAFE_FIRST, enabled)
        state.value = read()
    }

    private fun read(): TrekSettings {
        val ordinal = prefs.readInt(TrekPreferences.KEY_DEFAULT_TAP, DefaultTap.REVEAL.ordinal)
        val tap = DefaultTap.entries.getOrElse(ordinal) { DefaultTap.REVEAL }
        return TrekSettings(
            defaultTap = tap,
            safeFirstStep = prefs.readBoolean(TrekPreferences.KEY_SAFE_FIRST, true)
        )
    }
}
