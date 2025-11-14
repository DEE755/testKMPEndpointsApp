package com.example.demokmpinterfacetestingapp.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
expect fun GoogleSignInButton(
    serverClientId: String,
    backendUrl: String,
    onResult: (Boolean, String?) -> Unit = { _, _ -> }
)