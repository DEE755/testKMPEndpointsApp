package com.example.demokmpinterfacetestingapp.Screens

import com.example.demokmpinterfacetestingapp.Const.GoogleSignInParams
import com.example.demokmpinterfacetestingapp.Navigation.Router
import com.example.demokmpinterfacetestingapp.Navigation.Screen
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.demokmpinterfacetestingapp.ViewModel.LogInOutViewModel
import com.example.demokmpinterfacetestingapp.components.PickImageButton
import com.example.demokmpinterfacetestingapp.components.GoogleSignInButton

import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.logInOutViewModel






var result = ""

@Composable
fun LoginScreen(viewModel: LogInOutViewModel=logInOutViewModel, navRouter: Router? = null) {
    //val viewModel = viewModel ?: remember{ LogInOutViewModel(authRepository, userRepository) }
    val navRouter = navRouter ?: remember { Router(Screen.SignUpScreen) }
    val uiState by viewModel.uiState.collectAsState()
    val connectionStatus by viewModel.sessionManager.connectionStatus.collectAsState()
    val passwordVisible = remember { mutableStateOf(false) }

   //will launch in vm only if it has token saved from initialization
    LaunchedEffect(Unit) {
        viewModel.tryAndGetUserFromToken()
    }



    LaunchedEffect(connectionStatus.isConnected, uiState.currentUser?.username) {
        if (connectionStatus.isConnected && uiState.currentUser?.username?.isNotEmpty() == true) {
            navRouter.navigate(Screen.AppSelectionScreen)
        }
    }


        Column(modifier = Modifier.padding(30.dp)) {
            Spacer(modifier = Modifier.height(170.dp))


            Text("Sing In with email")
            Spacer(modifier = Modifier.height(25.dp))

            TextField(value = uiState.email, onValueChange = viewModel::onEmailChange, placeholder = { Text("Enter your email") })
            TextField(value = uiState.password,
                        onValueChange = viewModel::onPasswordChange,
                placeholder = { Text("Enter your password") },
                visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                        //val icon = if (passwordVisible.value) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                        //Icon(imageVector = icon, contentDescription = if (passwordVisible.value) "Hide" else "Show")
                    }
                })

            Button(
                onClick = viewModel::emailSignIn,
                enabled = !uiState.isLoading && uiState.validEmail && uiState.password.length >=3,
            ) {
                Text(if (uiState.isLoading) "Loading..." else "Sign in")

            }

            if (!uiState.validEmail && uiState.email.isNotEmpty())
            Text("${uiState.email} is not a valid email address", color = Color.Red)


            if (connectionStatus.error?.message.isNullOrEmpty().not() && connectionStatus.isConnected.not()) {
                Text("Error: ${connectionStatus.error?.message}", color = Color.Red)
            }


            Spacer(modifier = Modifier.height(230.dp))

            Text("Other sign in methods:")
            Spacer(modifier = Modifier.height(10.dp))

            Row {
                 GoogleSignInButton(
                    serverClientId = GoogleSignInParams.serverClientId,
                    backendUrl = GoogleSignInParams.backendUrl,
                    onSuccess = {
                        println("ConnectionStatus: $connectionStatus")
                        print("ConnectionERROR ? $connectionStatus.error")
                        if (connectionStatus.isConnected) {

                        navRouter.navigate(Screen.AppSelectionScreen)
                    } },
                )

                Spacer(modifier = Modifier.width(15.dp))

                Button(
                    onClick = { navRouter.navigate(Screen.SignInWithSMS) },
                    enabled = !uiState.isLoading
                ) {
                    Text(if (uiState.isLoading) "Loading..." else "Sign in with SMS")

                }
            }
            Spacer(modifier = Modifier.height(30.dp))

            Text("Don't have an account yet?")

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { navRouter.navigate(Screen.SignUpScreen) },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (uiState.isLoading) "Loading..." else "Sign up")
            }


            PickImageButton { img ->
                viewModel.uploadAppImage(
                    image = img,
                    folder = "app-images",
                    fileBasename = "app-logo"
                )
            }

        }
    }
