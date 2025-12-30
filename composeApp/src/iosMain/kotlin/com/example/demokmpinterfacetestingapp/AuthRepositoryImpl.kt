package com.example.demokmpinterfacetestingapp

import com.example.demokmpinterfacetestingapp.repository.AuthRepository
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

class AuthRepositoryImpl : AuthRepository {
    private val client = HttpClient() {
        install(ContentNegotiation) {
            json()
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = Logger.DEFAULT
        }
    }

    @Serializable
    data class MobilePhoneRequest(val mobile_number: String)

    @Serializable
    data class CodeVerificationRequest(val to: String, val code: String)

    override suspend fun sendMobilePhone(phone: String): String {
        val request = MobilePhoneRequest(phone)
        //println("Request sent: ${Json.encodeToString(MobilePhoneRequest.serializer(), request)}")
        val response: HttpResponse = client.post("http://127.0.0.1:7001/auth/send-signup-sms") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return response.bodyAsText()
    }

    override suspend fun verifyCode(to: String, code: String): String {
        val request = CodeVerificationRequest(to, code)
        //println("Code verification request: ${Json.encodeToString(CodeVerificationRequest.serializer(), request)}")
        val response: HttpResponse = client.post("http://127.0.0.1:7001/auth/verify-signup-otp") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return response.bodyAsText()
    }


}
