package app

import com.example.demokmpinterfacetestingapp.Screens.SignInWithSMSScreen
import com.example.demokmpinterfacetestingapp.Screens.SignUpScreen
import com.example.demokmpinterfacetestingapp.ViewModel.AuthViewModel
import com.example.demokmpinterfacetestingapp.Screens.LoginScreen
import com.example.demokmpinterfacetestingapp.Navigation.NavHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.demokmpinterfacetestingapp.ConnectedTempScreen
import com.example.demokmpinterfacetestingapp.Screens.AppBrowseScreen
import com.example.demokmpinterfacetestingapp.Screens.AppCreationScreen
import com.example.demokmpinterfacetestingapp.Screens.AppSelectionScreen
import com.example.demokmpinterfacetestingapp.Screens.PromptFromUserSeriesScreen
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.authRepository
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.userCloudRepository
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.cloudFilesRepository
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.sessionManager
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.navRouter
import com.example.demokmpinterfacetestingapp.Navigation.Screen

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
