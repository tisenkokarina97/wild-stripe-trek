package com.Qapfoxo9.Diknos.core.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.Qapfoxo9.Diknos.data.local.TrekPreferences
import com.Qapfoxo9.Diknos.data.repository.ProgressRepositoryImpl
import com.Qapfoxo9.Diknos.data.repository.SettingsRepositoryImpl
import com.Qapfoxo9.Diknos.domain.repository.ProgressRepository
import com.Qapfoxo9.Diknos.domain.repository.SettingsRepository

object ServiceLocator {

    private var viewModelFactory: ViewModelProvider.Factory? = null

    fun init(context: Context) {
        if (viewModelFactory != null) return
        val preferences = TrekPreferences(context)
        val progress: ProgressRepository = ProgressRepositoryImpl(preferences)
        val settings: SettingsRepository = SettingsRepositoryImpl(preferences)
        viewModelFactory = AppViewModelFactory(progress, settings)
    }

    val factory: ViewModelProvider.Factory
        get() = requireNotNull(viewModelFactory)
}
