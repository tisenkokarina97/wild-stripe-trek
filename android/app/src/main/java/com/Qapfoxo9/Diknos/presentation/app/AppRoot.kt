package com.Qapfoxo9.Diknos.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Qapfoxo9.Diknos.core.config.GameConfig
import com.Qapfoxo9.Diknos.core.di.ServiceLocator
import com.Qapfoxo9.Diknos.core.navigation.Screen
import com.Qapfoxo9.Diknos.core.theme.BgBase
import com.Qapfoxo9.Diknos.presentation.collection.CollectionRoute
import com.Qapfoxo9.Diknos.presentation.game.GameRoute
import com.Qapfoxo9.Diknos.presentation.levels.LevelSelectRoute
import com.Qapfoxo9.Diknos.presentation.menu.MenuRoute
import com.Qapfoxo9.Diknos.presentation.progress.ProgressRoute
import com.Qapfoxo9.Diknos.presentation.result.ResultRoute
import com.Qapfoxo9.Diknos.presentation.settings.SettingsRoute
import com.Qapfoxo9.Diknos.presentation.splash.SplashRoute

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = ServiceLocator.factory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()

    BackHandler(enabled = screen != Screen.Splash && screen != Screen.Menu) {
        appViewModel.back()
    }

    AnimatedContent(
        targetState = screen,
        modifier = Modifier
            .fillMaxSize()
            .background(BgBase),
        transitionSpec = {
            (
                fadeIn(animationSpec = tween(GameConfig.SCREEN_FADE_IN_MS)) +
                    slideInVertically(
                        animationSpec = tween(GameConfig.SCREEN_FADE_IN_MS),
                        initialOffsetY = { full -> full / 10 }
                    )
                ) togetherWith fadeOut(animationSpec = tween(GameConfig.SCREEN_FADE_OUT_MS))
        },
        label = "appScreens"
    ) { target ->
        when (target) {
            Screen.Splash -> SplashRoute(
                onFinished = { appViewModel.openMenu() }
            )

            Screen.Menu -> MenuRoute(
                onPlay = { level -> appViewModel.startRound(level) },
                onLevels = { appViewModel.navigate(Screen.Levels) },
                onJournal = { appViewModel.navigate(Screen.Journal) },
                onExpedition = { appViewModel.navigate(Screen.Expedition) },
                onOptions = { appViewModel.navigate(Screen.Options) }
            )

            Screen.Levels -> LevelSelectRoute(
                onBack = { appViewModel.openMenu() },
                onLevel = { level -> appViewModel.startRound(level) }
            )

            is Screen.Game -> GameRoute(
                levelIndex = target.levelIndex,
                roundId = target.roundId,
                onLeave = { appViewModel.openMenu() },
                onFinished = { result -> appViewModel.finishRound(result) }
            )

            is Screen.Result -> ResultRoute(
                result = target,
                onPlayAgain = { appViewModel.replayRound(target.levelIndex) },
                onNextTrail = { appViewModel.replayRound(target.levelIndex + 1) },
                onMenu = { appViewModel.openMenu() },
                onJournal = { appViewModel.navigate(Screen.Journal) }
            )

            Screen.Journal -> CollectionRoute(
                onBack = { appViewModel.back() }
            )

            Screen.Expedition -> ProgressRoute(
                onBack = { appViewModel.back() }
            )

            Screen.Options -> SettingsRoute(
                onBack = { appViewModel.back() }
            )
        }
    }
}
