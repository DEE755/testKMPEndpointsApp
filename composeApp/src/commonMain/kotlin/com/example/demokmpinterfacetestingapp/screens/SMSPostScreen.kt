package com.example.demokmpinterfacetestingapp.screens

import androidx.compose.runtime.Composable
import com.example.demokmpinterfacetestingapp.PostButtonScreen
import com.example.demokmpinterfacetestingapp.repository.AuthRepository

@Composable
fun SMSButtonScreen(repository: AuthRepository) {
    PostButtonScreen(repository)
}
