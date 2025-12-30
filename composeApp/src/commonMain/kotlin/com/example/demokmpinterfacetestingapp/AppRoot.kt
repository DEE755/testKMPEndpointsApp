package app

import com.example.demokmpinterfacetestingapp.screens.SignInWithSMSScreen
import com.example.demokmpinterfacetestingapp.screens.SignUpScreen
import com.example.demokmpinterfacetestingapp.screens.LoginScreen
import com.example.demokmpinterfacetestingapp.navigation.NavHost
import androidx.compose.runtime.Composable
import com.example.demokmpinterfacetestingapp.ConnectedTempScreen
import com.example.demokmpinterfacetestingapp.screens.AppBrowseScreen
import com.example.demokmpinterfacetestingapp.screens.AppCreationScreen
import com.example.demokmpinterfacetestingapp.screens.AppSelectionScreen
import com.example.demokmpinterfacetestingapp.screens.PromptFromUserSeriesScreen
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.navRouter

@Composable
fun AppRoot() {

    NavHost(
        router = navRouter,
        signUp = {
            SignUpScreen(navRouter = navRouter)
        },
        login = { LoginScreen(navRouter = navRouter) },
        smsSignUp = { SignInWithSMSScreen() },
        connectedTempScreen = {
            ConnectedTempScreen(
                navRouter = navRouter
            )
        },
        appSelectionScreen = { AppSelectionScreen() },
        appCreationScreen = { AppCreationScreen() },
        appBrowseScreen = { AppBrowseScreen()},
        promptFromUserSeriesScreen = { routerParam, questionsAnswerMap, functionToExecute ->
            PromptFromUserSeriesScreen(
                navRouter = routerParam,
                questionsAnswersMap = questionsAnswerMap,
                onAllQuestionsAnswered = functionToExecute
            )
        },
    )
}
