package com.example.demokmpinterfacetestingapp.components

import androidx.compose.runtime.Composable
import com.example.demokmpinterfacetestingapp.constants.GoogleSignInParams


@Composable

fun ContainerGoogleSignInButton( onSuccess: () -> Unit) {
    GoogleSignInButton(
        serverClientId = GoogleSignInParams.serverClientId,
        backendUrl = GoogleSignInParams.backendUrl,
        onSuccess = { onSuccess() }
    )
}

@Composable
expect fun GoogleSignInButton(
    serverClientId: String,
    backendUrl: String,
    onSuccess: () -> Unit
)