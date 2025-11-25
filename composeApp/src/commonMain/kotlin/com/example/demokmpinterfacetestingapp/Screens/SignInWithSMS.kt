package com.example.demokmpinterfacetestingapp.Screens

import com.example.demokmpinterfacetestingapp.ViewModel.LogInOutViewModel
import com.example.demokmpinterfacetestingapp.Navigation.Router
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import di.ServiceLocator.authRepository
import di.ServiceLocator.userRepository
import di.ServiceLocator.logInOutViewModel


@Composable
fun SignInWithSMSScreen(viewModel: LogInOutViewModel = logInOutViewModel, navRouter: Router? = null) {
    val uiState by viewModel.uiState.collectAsState()
    //val viewModel = viewModel ?: remember{LogInOutViewModel(authRepository, userRepository)}

    Column {
        Spacer(modifier = Modifier.height(30.dp))

        Text("Sign in with phone number")
        TextField(value = uiState.phoneNumber, onValueChange = viewModel::onPhoneNumberChange)
        Button(
            onClick = viewModel::onSMSSignUpClick,
            enabled = !uiState.isLoading
        ) {
            Text(if (uiState.isLoading) "Loading..." else "Send code")

            Button(
                onClick = {
                    navRouter?.backToLoginScreen()

                }
            ) { Text("Login with different method")}

        }
    }

}