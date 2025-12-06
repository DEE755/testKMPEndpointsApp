package com.example.demokmpinterfacetestingapp.components

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch
import java.util.UUID
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.logInOutViewModel
import kotlinx.coroutines.runBlocking


@Composable
actual fun GoogleSignInButton(
    serverClientId: String,
    backendUrl: String,
    onSuccess: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(StartActivityForResult()) { result: ActivityResult ->
        coroutineScope.launch {
            val data: Intent? = result.data
            if (result.resultCode != Activity.RESULT_OK || data == null) {
                throw IllegalStateException("Google sign-in failed")

            }

            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken
                if (idToken.isNullOrBlank()) {
                    logInOutViewModel.showErrorMessage("Google ID token is null or blank")
                    return@launch
                }

                val nonce = UUID.randomUUID().toString()

                runBlocking {
                    logInOutViewModel.googleSignIn(idToken, nonce)
                    onSuccess.invoke()

                }


            } catch (e: ApiException) {
                throw IllegalStateException("Server Error :${e.message}")

            } catch (e: Exception) {
                throw IllegalStateException("Google sign-in failed: ${e.message}")

            }
        }
    }

    val client = remember(serverClientId) {
        GoogleSignIn.getClient(
            context as Activity,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(serverClientId)
                .requestEmail()
                .build()
        )
    }

    Button(onClick = {
        val intent = client.signInIntent
        launcher.launch(intent)
    }) {
        Text(text = "Sign in with Google")
    }
}