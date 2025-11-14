package com.example.demokmpinterfacetestingapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Button
import androidx.compose.material3.Text

@Composable
actual fun GoogleSignInButton(
    modifier: Modifier,
    onClick: () -> Unit
) {
    Button(modifier = modifier, onClick = onClick) {
        Text("Sign in with Google")
    }
}

