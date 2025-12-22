package com.example.demokmpinterfacetestingapp.Screens.Menus

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.navRouter
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.authViewModel
import com.example.demokmpinterfacetestingapp.Navigation.Screen
import com.example.demokmpinterfacetestingapp.components.CardButton
import com.example.demokmpinterfacetestingapp.components.ContainerGoogleSignInButton
import com.example.demokmpinterfacetestingapp.ui.showToast
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@Composable
fun SignInSignUpSliderHost() {
    val viewModel = authViewModel
    val connectionStatus by viewModel.sessionManager.connectionStatus.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    if (uiState.isLoading) {
        Column(modifier = Modifier.padding(100.dp)) {
            CircularProgressIndicator()
            Text("Loading user data...")
        }
        return
    }

    Column {

        CardButton({ navRouter.navigate(Screen.LoginScreen) }) {
            Text("< Sign In")
        }

        CardButton({ scope.launch { showToast("Not Implemented Yet") } }) {
            Text("< Contact Us")
        }

        CardButton({ scope.launch { showToast("Not Implemented Yet") } }) {
            Text("< About")
        }

        Spacer(Modifier.height(200.dp))

        CardButton({ navRouter.navigate(Screen.SignUpScreen) }) {
            Text("< New Account")
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center)  {
                Column {
                    Text("Or sign in with")
                    Spacer(Modifier.height(8.dp))

                    Row {
                        KamelImage(
                            resource = asyncPainterResource("https://upload.wikimedia.org/wikipedia/commons/f/fa/Apple_logo_black.svg") ,
                            contentDescription = "apple logo",
                            modifier = Modifier
                                .height(50.dp).clickable(onClick = {scope.launch { showToast("Not Implemented Yet")} }
                                ).width(50.dp)
                        )

                        Spacer(Modifier.width(16.dp))

                        ContainerGoogleSignInButton {
                            if (connectionStatus.isConnected) {
                                navRouter.navigate(Screen.AppSelectionScreen)
                            }
                        }



                        }
                    }
                }
            }
        }
    }





@Composable
fun SliderConnected() {
    val viewModel = authViewModel
    val connectionStatus by viewModel.sessionManager.connectionStatus.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    if (uiState.isLoading) {
        Column(modifier = Modifier.padding(100.dp)) {
            CircularProgressIndicator()
            Text("Loading user data...")
        }
        return
    }

    Column {
        Spacer(Modifier.height(100.dp))

        CardButton( {scope.launch { showToast("Not Implemented Yet")} }) {

            Text("< Reset Password")
        }


        CardButton( {scope.launch { showToast("Not Implemented Yet")} }) {

            Text(" < Contact Us")
        }

        CardButton( {scope.launch { showToast("Not Implemented Yet")} }) {

            Text(" < About")
        }


        Spacer(Modifier.height(200.dp))
        CardButton(
            {runBlocking {
                authViewModel.logout()
                navRouter.navigate(Screen.AppSelectionScreen)
            }}) {
            Text("LogOut")
        }
    }
}
