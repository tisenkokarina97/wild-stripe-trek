package com.Qapfoxo9.Diknos.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Qapfoxo9.Diknos.domain.repository.ProgressRepository
import com.Qapfoxo9.Diknos.domain.repository.SettingsRepository
import com.Qapfoxo9.Diknos.domain.usecase.GenerateBoardUseCase
import com.Qapfoxo9.Diknos.domain.usecase.RevealTileUseCase
import com.Qapfoxo9.Diknos.domain.usecase.ScoreRoundUseCase
import com.Qapfoxo9.Diknos.presentation.app.AppViewModel
import com.Qapfoxo9.Diknos.presentation.collection.CollectionViewModel
import com.Qapfoxo9.Diknos.presentation.game.GameViewModel
import com.Qapfoxo9.Diknos.presentation.levels.LevelSelectViewModel
import com.Qapfoxo9.Diknos.presentation.menu.MenuViewModel
import com.Qapfoxo9.Diknos.presentation.progress.ProgressViewModel
import com.Qapfoxo9.Diknos.presentation.result.ResultViewModel
import com.Qapfoxo9.Diknos.presentation.settings.SettingsViewModel
import com.Qapfoxo9.Diknos.presentation.splash.SplashViewModel

class AppViewModelFactory(
    private val progress: ProgressRepository,
    private val settings: SettingsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) -> AppViewModel()
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()
            modelClass.isAssignableFrom(MenuViewModel::class.java) ->
                MenuViewModel(progress, settings)
            modelClass.isAssignableFrom(LevelSelectViewModel::class.java) ->
                LevelSelectViewModel(progress)
            modelClass.isAssignableFrom(GameViewModel::class.java) ->
                GameViewModel(
                    progress,
                    settings,
                    GenerateBoardUseCase(),
                    RevealTileUseCase(),
                    ScoreRoundUseCase()
                )
            modelClass.isAssignableFrom(ResultViewModel::class.java) ->
                ResultViewModel(progress)
            modelClass.isAssignableFrom(CollectionViewModel::class.java) ->
                CollectionViewModel(progress)
            modelClass.isAssignableFrom(ProgressViewModel::class.java) ->
                ProgressViewModel(progress)
            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(progress, settings)
            else -> throw IllegalArgumentException(modelClass.name)
        }
        return created as T
    }
}
