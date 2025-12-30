package com.example.demokmpinterfacetestingapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun NavHost(
    router: Router,
    signUp: @Composable (router: Router) -> Unit,
    login: @Composable (router: Router) -> Unit,
    smsSignUp: @Composable (router: Router) -> Unit,
    connectedTempScreen: @Composable (router: Router) -> Unit,
    appSelectionScreen: @Composable (router: Router) -> Unit,
    appCreationScreen: @Composable (router: Router) -> Unit,
    appBrowseScreen: @Composable (router: Router) -> Unit,
    promptFromUserSeriesScreen: @Composable (router: Router, questionAnswerMap: MutableMap<String,String>, functionToExecute: () -> Unit) -> Unit
) {
    val screen by router.current.collectAsState()
    when (screen) {
        is Screen.SignUpScreen -> signUp(router)
        is Screen.LoginScreen -> login(router)
        is Screen.SignInWithSMS -> smsSignUp(router)
        is Screen.ConnectedTempScreen -> connectedTempScreen(router)

        is Screen.AppSelectionScreen -> appSelectionScreen(router)
        is Screen.AppBrowseScreen -> appBrowseScreen(router)
        is Screen.AppCreationScreen -> appCreationScreen(router)
        is Screen.PromptFromUserSeriesScreen -> {
            val s = screen as Screen.PromptFromUserSeriesScreen

            promptFromUserSeriesScreen(router, s.questionAnswerMap ,s.functionToExecute)
        }
        else -> {}
    }
}