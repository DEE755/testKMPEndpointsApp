package com.example.demokmpinterfacetestingapp.components

import androidx.compose.runtime.Composable


@Composable
expect fun GoogleSignInButton(
    serverClientId: String,
    backendUrl: String,
    onSuccess: () -> Unit
)