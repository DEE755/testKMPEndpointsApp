package app

import com.example.demokmpinterfacetestingapp.Screens.SignInWithSMSScreen
import com.example.demokmpinterfacetestingapp.Screens.SignUpScreen
import com.example.demokmpinterfacetestingapp.ViewModel.LogInOutViewModel
import com.example.demokmpinterfacetestingapp.LoginScreen
import com.example.demokmpinterfacetestingapp.Navigation.NavHost
import com.example.demokmpinterfacetestingapp.Navigation.Router
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.demokmpinterfacetestingapp.ConnectedTempScreen
import com.example.demokmpinterfacetestingapp.Screens.AppBrowseScreen
import com.example.demokmpinterfacetestingapp.Screens.AppCreationScreen
import com.example.demokmpinterfacetestingapp.Screens.AppSelectionScreen
import com.example.demokmpinterfacetestingapp.Screens.PromptFromUserSeriesScreen
import di.ServiceLocator.authRepository
import di.ServiceLocator.userRepository
import di.ServiceLocator.cloudFilesRepository

@Composable
fun AppRoot() {
    val navRouter = remember { Router() }
    val viewModel = remember{LogInOutViewModel(authRepository, userRepository, cloudFilesRepository)}
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
        appSelectionScreen = { AppSelectionScreen(viewModel, navRouter) },
        appCreationScreen = { AppCreationScreen() },
        appBrowseScreen = { AppBrowseScreen()},
        promptFromUserSeriesScreen = { routerParam, questionsList, functionToExecute ->
            PromptFromUserSeriesScreen(
                questionsList = questionsList,
                viewModel = viewModel,
                navRouter = routerParam,
                functionToExecute = functionToExecute
            )
        },
    )
}
