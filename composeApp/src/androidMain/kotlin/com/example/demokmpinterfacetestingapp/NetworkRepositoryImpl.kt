package com.example.demokmpinterfacetestingapp

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.http.*
import android.util.Log

class NetworkRepositoryImpl : NetworkRepository {
    private val client = HttpClient(io.ktor.client.engine.okhttp.OkHttp) {
        install(ContentNegotiation) {
            json()
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = Logger.DEFAULT
        }
    }

    override suspend fun sendMobilePhone(phone: String): String {
        val request = MobilePhoneRequest(phone)
        Log.d("NetworkRepositoryImpl", "Request sent: ${kotlinx.serialization.json.Json.encodeToString(MobilePhoneRequest.serializer(), request)}")
        val response: HttpResponse = client.post("http://127.0.0.1:7001/auth/send-signup-sms") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return response.bodyAsText()
    }
}
