package com.example.demokmpinterfacetestingapp

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

class NetworkRepositoryImpl : NetworkRepository {
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
}
