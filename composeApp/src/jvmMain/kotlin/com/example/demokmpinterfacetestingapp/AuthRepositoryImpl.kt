package com.example.demokmpinterfacetestingapp

import com.example.demokmpinterfacetestingapp.Repository.AuthRepository
import com.example.demokmpinterfacetestingapp.Model.models.requests.CodeVerificationRequest
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

class AuthRepositoryImpl : AuthRepository {
    private val client = HttpClient() {
        install(ContentNegotiation) {
            json()
        }
    }

    @Serializable
    data class MobilePhoneRequest(val mobile_number: String)

    override suspend fun sendMobilePhone(phone: String): String {
        val request = MobilePhoneRequest(phone)
        println("Request sent: $request")
        val response: HttpResponse = client.post("http://127.0.0.1:7001/auth/send-signup-sms") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return response.bodyAsText()
    }

    override suspend fun verifyCode(to: String, code: String): String {
        val request = CodeVerificationRequest(to, code)
        println("Code verification request: $request")
        val response: HttpResponse = client.post("http://127.0.0.1:7001/auth/verify-signup-otp") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return response.bodyAsText()
    }
}
