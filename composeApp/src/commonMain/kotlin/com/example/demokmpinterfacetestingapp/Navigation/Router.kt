package com.example.demokmpinterfacetestingapp.Navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class Router(initial: Screen = Screen.LoginScreen) {
    private val _current = MutableStateFlow(initial)
    val current: StateFlow<Screen> = _current

    fun navigate(to: Screen) { _current.value = to }

    //fun navigate2Args(to: Screen, Any<*>, Any<*>) { _current.value = to }
    fun backToLoginScreen() { _current.value = Screen.LoginScreen }
}
