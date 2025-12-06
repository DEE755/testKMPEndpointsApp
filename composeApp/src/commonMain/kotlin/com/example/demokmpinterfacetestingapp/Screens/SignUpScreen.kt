package com.example.demokmpinterfacetestingapp.Screens

import com.example.demokmpinterfacetestingapp.ViewModel.LogInOutViewModel
import com.example.demokmpinterfacetestingapp.Navigation.Router
import com.example.demokmpinterfacetestingapp.Navigation.Screen
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.demokmpinterfacetestingapp.Const.GoogleSignInParams
import com.example.demokmpinterfacetestingapp.Model.models.responses.GoogleSignInResponse
import com.example.demokmpinterfacetestingapp.components.GoogleSignInButton
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.logInOutViewModel
import kotlinx.coroutines.runBlocking

@Composable
fun SignUpScreen(navRouter: Router? = null, viewModel: LogInOutViewModel = logInOutViewModel) {
    val navRouter = navRouter ?: remember { Router(Screen.LoginScreen) }
    val uiState by viewModel.uiState.collectAsState()
    val connectionStatus by viewModel.sessionManager.connectionStatus.collectAsState()
    val passwordVisible = remember { mutableStateOf(false) }

    lateinit var userInfo: GoogleSignInResponse

    if (uiState.currentUser?.username?.isNotEmpty() == true && connectionStatus.isConnected) {
        navRouter.navigate(Screen.AppSelectionScreen)
    }

    Column(modifier = Modifier.padding(30.dp)) {
        Spacer(modifier = Modifier.height(170.dp))

        Text("Sing Up with email")
        Spacer(modifier = Modifier.height(25.dp))

        TextField(
            value = uiState.email,
            onValueChange = viewModel::onEmailChange,
            placeholder = { Text("Enter your email") })
        TextField(
            value = uiState.emailConfirmation,
            onValueChange = viewModel::onEmailConfirmationChange,
            placeholder = { Text("Enter your email again") })
        TextField(
            value = uiState.password,
            onValueChange = viewModel::onPasswordChange,
            placeholder = { Text("Enter your password") },
            visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                    //val icon = if (passwordVisible.value) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    //Icon(imageVector = icon, contentDescription = if (passwordVisible.value) "Hide" else "Show")
                }
            })

        if (uiState.email != uiState.emailConfirmation && uiState.emailConfirmation.isNotEmpty()) {
            Text("Email and confirmation do not match", color = Color.Red)
        }

        if (!uiState.password.isEmpty() && uiState.validPassword.not())
            Text(
                "Password must contain both number and special character, and must be at least 8 characters",
                color = Color.Red
            )


        Button(
            onClick = {
                navRouter.navigate(
                    Screen.PromptFromUserSeriesScreen(
                        viewModel.signUpQuestionsAnswersMap,
                        {
                            viewModel.signUpQuestionsAnswersMap.values.first().let {
                                runBlocking {
                                    viewModel.setUsername(it)
                                    viewModel.emailSignUp()

                                }
                                if (connectionStatus.isConnected) {
                                    navRouter.navigate(
                                        Screen.AppSelectionScreen
                                    )
                                }
                            }
                        }
                    )
                )
            },
            enabled = !uiState.isLoading && uiState.validEmail && uiState.validPassword,
        ) {
            Text(if (uiState.isLoading) "Loading..." else "Sign up")

        }

        if (!uiState.validEmail && uiState.email.isNotEmpty())
            Text("${uiState.email} is not a valid email address", color = Color.Red)


        if (connectionStatus.error?.message.isNullOrEmpty().not() && connectionStatus.isConnected.not()) {
            Text("Error: ${connectionStatus.error?.message}", color = Color.Red)
        }




        Spacer(modifier = Modifier.height(175.dp))

        Text("Other sign up methods:")

        Spacer(modifier = Modifier.height(10.dp))

        Row{ GoogleSignInButton(
            serverClientId = GoogleSignInParams.serverClientId,
            backendUrl = GoogleSignInParams.backendUrl,
            onSuccess = { if (connectionStatus.isConnected) {
                navRouter.navigate(Screen.AppSelectionScreen)
            }
            }
        )
            Spacer(modifier = Modifier.width(15.dp))
            Button(onClick = { navRouter.navigate(Screen.SignInWithSMS) },){
                Text("Sign up with SMS")
            }}

        Spacer(modifier = Modifier.height(30.dp))
       Column(modifier = Modifier.fillMaxSize()) {
           Text("Already have an account?")
           Spacer(modifier = Modifier.height(10.dp))
           Button(

               onClick = { navRouter.navigate(Screen.LoginScreen) }) {
                   Text("Sign in")
               }

       }


}

}