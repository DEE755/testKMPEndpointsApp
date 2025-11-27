package com.example.demokmpinterfacetestingapp.Navigation

import kotlin.reflect.KFunction0

sealed class Screen {
    object SignInWithSMS : Screen()
    object LoginScreen : Screen()
    object SignUpScreen : Screen()
    object ConnectedTempScreen : Screen()

    object AppSelectionScreen : Screen()

    object AppCreationScreen : Screen()

    object AppBrowseScreen : Screen()

    data class PromptFromUserSeriesScreen(
        val questionAnswerMap: MutableMap<String, String>,
        val functionToExecute: () -> Unit
    ) : Screen()



}