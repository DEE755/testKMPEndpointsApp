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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.demokmpinterfacetestingapp.Model.models.responses.GoogleSignInResponse
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.UUID

@Composable
actual fun GoogleSignInButton(//TODO(call VM->REPOSITORY
    serverClientId: String,
    backendUrl: String,
    onResult: (Boolean, GoogleSignInResponse?) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(StartActivityForResult()) { result: ActivityResult ->
        coroutineScope.launch {
            val data: Intent? = result.data
            if (result.resultCode != Activity.RESULT_OK || data == null) {
                onResult(false, GoogleSignInResponse("", "", "", "", errorMessage = "Cancelled or no intent", email_verified = false))
                return@launch
            }

            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken
                if (idToken.isNullOrBlank()) {
                    onResult(false, GoogleSignInResponse("", "", "", "", errorMessage = "No token returned", email_verified = false))
                    return@launch
                }

                val nonce = UUID.randomUUID().toString()

                val postResult = withContext(Dispatchers.IO) {
                    val clientHttp = OkHttpClient()
                    val json = JSONObject().apply {
                        put("id_token", idToken)
                        put("nonce", nonce)
                    }.toString()
                    val reqBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())
                    val req = Request.Builder().url(backendUrl).post(reqBody).build()
                    clientHttp.newCall(req).execute().use { resp ->
                        val body: GoogleSignInResponse = resp.body!!.string().let { respBody ->
                            val respJson = JSONObject(respBody)
                            GoogleSignInResponse(
                                token = respJson.getString("token"),
                                username = respJson.getString("username"),
                                email = respJson.getString("email"),
                                email_verified = respJson.getBoolean("email_verified"),
                                google_avatar_url = respJson.getString("google_avatar_url"),
                                user_id = respJson.getString("user_id"),
                                user_status = respJson.getString("user_status"),
                                errorMessage = "",
                            )
                        }
                        if (!resp.isSuccessful) throw Exception("HTTP ${resp.code}: $body")
                        body
                    }
                }
                onResult(true, postResult)
            } catch (e: ApiException) {
                onResult(false,GoogleSignInResponse(
                    errorMessage = "Google sign-in failed: ${e.statusCode}",
                ) )
            } catch (e: Exception) {
                onResult(false, GoogleSignInResponse(
                    errorMessage = "Google sign-in failed: ${e.message}",
                ))
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