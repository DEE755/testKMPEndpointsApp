package com.example.demokmpinterfacetestingapp.Repository

import android.util.Log
import com.example.demokmpinterfacetestingapp.Const.CustomApiParams
import com.example.demokmpinterfacetestingapp.Model.models.GoogleExtraUserInfo
import com.example.demokmpinterfacetestingapp.Model.models.requests.CodeVerificationRequest
import com.example.demokmpinterfacetestingapp.Model.models.requests.EmailSignInRequest
import com.example.demokmpinterfacetestingapp.Model.models.requests.MobilePhoneRequest
import com.example.demokmpinterfacetestingapp.Model.models.responses.SignInResponse

import com.example.demokmpinterfacetestingapp.Model.models.User
import com.example.demokmpinterfacetestingapp.Model.models.requests.EmailSignUpRequest
import com.example.demokmpinterfacetestingapp.Model.models.responses.CurrentUserResponse
import com.example.demokmpinterfacetestingapp.Model.models.responses.GoogleSignInResponse
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.tokenProvider
import com.example.demokmpinterfacetestingapp.DI.UserPrefsDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json

class AndroidAuthRepositoryAndroid(val client: HttpClient, val userPrefs: UserPrefsDataSource) : AuthRepository {

    val authBaseUrl: String = CustomApiParams.getBaseUrl() + "/auth"



    override suspend fun getCurrentUserFromCloud(): User? {
        val response: HttpResponse = client.get("$authBaseUrl/get_user_from_token") {
            headers.append("Content-Type", ContentType.Application.Json.toString())
            // token is added automatically from the AuthInterceptor
        }

        if (!response.status.isSuccess()) {
            throw Exception("Failed to get user: ${response.status}")
        }

        val parsed = Json { ignoreUnknownKeys = true }
            .decodeFromString(CurrentUserResponse.serializer(), response.bodyAsText())
        parsed.user?.let{saveUserInPrefs(it)}
        return parsed.user
    }

    override suspend fun googleSignIn(idToken: String, nonce: String?): User {
        val response: HttpResponse = client.post("$authBaseUrl/google/verify") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("id_token" to idToken, "nonce" to nonce))
        }

        if (!response.status.isSuccess()) {
            throw Exception("Failed to sign in with Google: ${response.status}")
        }

       if (response.status.isSuccess()) println("SUCCESSSSSS")

        val parsed = Json { ignoreUnknownKeys = true }
            .decodeFromString(GoogleSignInResponse.serializer(), response.bodyAsText())

        val userId = parsed.user_id ?: throw IllegalStateException("Google Sign-In response missing user_id")
        val username = parsed.username ?: throw IllegalStateException("Google Sign-In response missing username")
        val token = parsed.token ?: throw IllegalStateException("Google Sign-In response missing token")
        val email = parsed.email ?: throw IllegalStateException("Google Sign-In response missing email")
        val avatarUrl = parsed.google_avatar_url
        val emailVerified =
            parsed.email_verified ?: throw IllegalStateException("Google Sign-In response missing email_verified")

        val googleUser = User(
            _id = userId,
            username = username,
            email = email,
            token = token,
            avatarURL = avatarUrl,
            googleUserInfo = GoogleExtraUserInfo(
                name = username,
                picture = avatarUrl,
                email_verified = emailVerified
            )
        )
        saveAccessToken(token)
        saveUserInPrefs(googleUser)
        saveGoogleUserInPrefs(googleUser)

        return googleUser
    }

    override suspend fun saveAccessToken(token: String?) {
        tokenProvider.saveAccessToken(token)
        tokenProvider.hasBearerSet = true
    }


    override suspend fun sendMobilePhone(phone: String): String {
        val request = MobilePhoneRequest(phone)
        Log.d("NetworkRepositoryImpl", "Request sent: ${
            Json.encodeToString(
            MobilePhoneRequest.serializer(), request)}")
        val response: HttpResponse = client.post("$authBaseUrl/send-signup-sms") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return response.bodyAsText()
    }

    override suspend fun verifyCode(to: String, code: String): String {
        val request = CodeVerificationRequest(to, code)
        Log.d("NetworkRepositoryImpl", "Code verification request: ${
            Json.encodeToString(
            CodeVerificationRequest.serializer(), request)}")
        val response: HttpResponse = client.post("$authBaseUrl/verify-signup-otp") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return response.bodyAsText()
    }

    override suspend fun emailSignIn(email: String, password: String): User? {
        var parsedUser : User? = null
        var parsedSignInResponse : SignInResponse? = null

            val request = EmailSignInRequest(email, password)
            val response: HttpResponse = client.post("$authBaseUrl/signin") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (!response.status.isSuccess()) {
                throw Exception("Failed to sign in: ${response.status}")
            }

            parsedSignInResponse = Json.Default.decodeFromString(response.bodyAsText())
            //parsedUser = Json.decodeFromString<User?>(response.bodyAsText())
            parsedUser = parsedSignInResponse?.user
            Log.d("NetworkRepositoryImpl", "Success: $parsedUser")
            parsedUser?.let{saveUserInPrefs(it)}
            return parsedUser

    }


    override suspend fun emailSignUp(email: String, password : String, username: String): User? {

        val request = EmailSignUpRequest(email, password, username)
        val response: HttpResponse = client.post("$authBaseUrl/signup") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }


        if (!response.status.isSuccess()) {
            throw Exception("Failed to sign up: ${response.status}")
        }

        Log.d("NetworkRepositoryImpl", "Email sign-up: test " )

        val parsed = try {
            Json { ignoreUnknownKeys = true }
                .decodeFromString(SignInResponse.serializer(), response.bodyAsText())
        } catch (e: Exception) {
            Log.e("NetworkRepositoryImpl", "Deserialization failed: ${response.bodyAsText()}", e)
            throw e
        }

        saveAccessToken(parsed.user.token)
        saveUserInPrefs(parsed.user)


        Log.d("NetworkRepositoryImpl", "Email sign-up: $parsed")

        return parsed.user
    }


    suspend fun saveUserInPrefs(user: User)
    {
        userPrefs.saveFullUserInCache(user)

    }

    suspend fun saveGoogleUserInPrefs(user: User)
    {
        userPrefs.saveFullGoogleUserInCache(user)

    }





}