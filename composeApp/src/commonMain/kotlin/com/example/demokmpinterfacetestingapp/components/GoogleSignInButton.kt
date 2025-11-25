package com.example.demokmpinterfacetestingapp.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.demokmpinterfacetestingapp.com.example.demokmpinterfacetestingapp.Model.models.responses.GoogleSignInResponse


@Composable
expect fun GoogleSignInButton(
    serverClientId: String,
    backendUrl: String,
    onResult: (Boolean, GoogleSignInResponse?) -> Unit = { _, _ -> }
)