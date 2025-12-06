package app

import com.example.demokmpinterfacetestingapp.Screens.SignInWithSMSScreen
import com.example.demokmpinterfacetestingapp.Screens.SignUpScreen
import com.example.demokmpinterfacetestingapp.ViewModel.LogInOutViewModel
import com.example.demokmpinterfacetestingapp.Screens.LoginScreen
import com.example.demokmpinterfacetestingapp.Navigation.NavHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
@Composable
fun AppRoot() {
   //initilize first time objects:
    val viewModel = remember{LogInOutViewModel(
        authRepository, userCloudRepository, cloudFilesRepository, sessionManager
    )}
    val uiState by viewModel.uiState.collectAsState()

    NavHost(
        router = navRouter,
        signUp = {
            SignUpScreen(viewModel = viewModel, navRouter = navRouter)
        },
        login = { LoginScreen(viewModel= viewModel, navRouter = navRouter) },
        smsSignUp = { SignInWithSMSScreen(viewModel) },
        connectedTempScreen = {
            ConnectedTempScreen(
                viewModel,
                navRouter = navRouter
            )
        },
        appSelectionScreen = { AppSelectionScreen() },
        appCreationScreen = { AppCreationScreen() },
        appBrowseScreen = { AppBrowseScreen()},
        promptFromUserSeriesScreen = { routerParam, questionsAnswerMap, functionToExecute ->
            PromptFromUserSeriesScreen(
                navRouter = routerParam,
                viewModel = viewModel,
                questionsAnswersMap = questionsAnswerMap,
                onAllQuestionsAnswered = functionToExecute
            )
        },
    )
}
