package com.Qapfoxo9.Diknos.presentation.app

import androidx.lifecycle.ViewModel
import com.Qapfoxo9.Diknos.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val current = MutableStateFlow<Screen>(Screen.Splash)

    val screen: StateFlow<Screen> = current.asStateFlow()

    private val backStack = ArrayDeque<Screen>()

    private var roundCounter = 0

    fun openMenu() {
        backStack.clear()
        current.value = Screen.Menu
    }

    fun navigate(target: Screen) {
        val previous = current.value
        if (previous != Screen.Splash) backStack.addLast(previous)
        current.value = target
    }

    fun startRound(levelIndex: Int) {
        roundCounter += 1
        navigate(Screen.Game(levelIndex, roundCounter))
    }

    fun replayRound(levelIndex: Int) {
        roundCounter += 1
        backStack.clear()
        backStack.addLast(Screen.Menu)
        current.value = Screen.Game(levelIndex, roundCounter)
    }

    fun finishRound(result: Screen.Result) {
        backStack.clear()
        backStack.addLast(Screen.Menu)
        current.value = result
    }

    fun back() {
        current.value = backStack.removeLastOrNull() ?: Screen.Menu
    }
}
