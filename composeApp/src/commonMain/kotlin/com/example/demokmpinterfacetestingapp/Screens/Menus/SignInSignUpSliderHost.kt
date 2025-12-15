package com.example.demokmpinterfacetestingapp.Screens.Menus

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.navRouter
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.authViewModel
import com.example.demokmpinterfacetestingapp.Navigation.Screen
import com.example.demokmpinterfacetestingapp.components.ContainerGoogleSignInButton
import kotlinx.coroutines.runBlocking


@Composable
fun SignInSignUpSliderHost() {
    val viewModel = authViewModel
    val connectionStatus by viewModel.sessionManager.connectionStatus.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        Column(modifier = Modifier.padding(100.dp)) {
            CircularProgressIndicator()
            Text("Loading user data...")
        }
        return
    }

    Column {
        Text("Welcome Guest!")
        Spacer(Modifier.height(100.dp))

        Button({ navRouter.navigate(Screen.LoginScreen) }) {
            Text("Sign In")
        }
        Spacer(Modifier.height(100.dp))

        Button({ navRouter.navigate(Screen.SignUpScreen) })
        {
            Text("Sign Up")
        }
        Spacer(Modifier.height(100.dp))


        ContainerGoogleSignInButton {
            if (connectionStatus.isConnected) {
                navRouter.navigate(Screen.AppSelectionScreen)
            }
        }

        Spacer(Modifier.height(100.dp))

        Button({
            runBlocking {
                authViewModel.logout()
                navRouter.navigate(Screen.LoginScreen)
            }
            //close drawer
        }) {
            Text("LogOut")
        }
    }
}


@Composable
fun SliderConnected() {
    val viewModel = authViewModel
    val connectionStatus by viewModel.sessionManager.connectionStatus.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        Column(modifier = Modifier.padding(100.dp)) {
            CircularProgressIndicator()
            Text("Loading user data...")
        }
        return
    }

    Column {
        Text("Currently Connected as ${viewModel.uiState.value.currentUser?.username?: "Guest"}")
        Spacer(Modifier.height(100.dp))


        Spacer(Modifier.height(100.dp))

        Button({
            runBlocking {
                authViewModel.logout()
                navRouter.navigate(Screen.LoginScreen)
            }
        }) {
            Text("LogOut")
        }
    }
}
