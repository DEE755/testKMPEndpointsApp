package com.example.demokmpinterfacetestingapp.Repository

import android.util.Log
import com.example.demokmpinterfacetestingapp.Const.CustomApiParams
import com.example.demokmpinterfacetestingapp.Model.models.requests.CodeVerificationRequest
import com.example.demokmpinterfacetestingapp.Model.models.requests.EmailSignInRequest
import com.example.demokmpinterfacetestingapp.Model.models.requests.MobilePhoneRequest
import com.example.demokmpinterfacetestingapp.Model.models.responses.SignInResponse
import com.example.demokmpinterfacetestingapp.Model.models.User
import com.example.demokmpinterfacetestingapp.Model.models.requests.EmailSignUpRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json

class AndroidAuthRepositoryAndroid(val client: HttpClient) : AuthRepository {

    val authBaseUrl: String = CustomApiParams.getBaseUrl() + "/auth"


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
            return parsedUser

    }


    override suspend fun emailSignUp(email: String, password : String, username: String){

        val request = EmailSignUpRequest(email, password, username)
        val response: HttpResponse = client.post("$authBaseUrl/signup") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        if (!response.status.isSuccess()) {
            throw Exception("Failed to sign up: ${response.status}")
        }
    }




}