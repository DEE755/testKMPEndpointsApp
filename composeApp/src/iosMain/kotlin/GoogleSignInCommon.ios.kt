package com.example.demokmpinterfacetestingapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun GoogleSignInButton(
    serverClientId: String,
    backendUrl: String,
    modifier: Modifier,
    onResult: (Boolean, String?) -> Unit
) {
}