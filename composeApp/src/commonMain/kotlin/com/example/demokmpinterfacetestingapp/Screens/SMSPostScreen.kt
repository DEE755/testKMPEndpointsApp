package com.example.demokmpinterfacetestingapp.Screens

import androidx.compose.runtime.Composable
import com.example.demokmpinterfacetestingapp.PostButtonScreen
import com.example.demokmpinterfacetestingapp.Repository.AuthRepository

@Composable
fun SMSButtonScreen(repository: AuthRepository) {
    PostButtonScreen(repository)
}
