package com.example.demokmpinterfacetestingapp.Screens

import com.example.demokmpinterfacetestingapp.ViewModel.AuthViewModel
import com.example.demokmpinterfacetestingapp.Navigation.Router
import com.example.demokmpinterfacetestingapp.Navigation.Screen
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.authViewModel

@Composable
fun UserCreateScreen(viewModel: AuthViewModel=authViewModel, navRouter: Router?) {
    //val viewModel = viewModel ?: remember{LogInOutViewModel(authRepository, userRepository)}
    val navRouter = navRouter ?: remember { Router(Screen.LoginScreen) }
    val uiState by viewModel.uiState.collectAsState()
    Text("Please let us know about yourself!")
    TextField(value = uiState.currentUser?.username?: "",
        onValueChange = {viewModel.onUsernameChange(uiState.currentUser?.username?:"")},
        placeholder = { Text("Enter your username") })
}