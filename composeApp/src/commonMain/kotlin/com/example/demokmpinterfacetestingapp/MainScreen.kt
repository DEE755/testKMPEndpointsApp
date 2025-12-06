package com.example.demokmpinterfacetestingapp

import com.example.demokmpinterfacetestingapp.Navigation.Screen
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import app.AppRoot
import com.example.demokmpinterfacetestingapp.ViewModel.LogInOutViewModel


val initial : Screen = Screen.LoginScreen

@Composable
fun MainScreen() {

    Column {
        AppRoot()
    }

}
