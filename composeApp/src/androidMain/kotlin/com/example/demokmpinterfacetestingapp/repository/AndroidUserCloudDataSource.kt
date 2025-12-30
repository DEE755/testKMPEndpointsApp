package com.example.demokmpinterfacetestingapp.repository

import com.example.demokmpinterfacetestingapp.Model.models.User
import com.example.demokmpinterfacetestingapp.Model.models.requests.UsernameUpdateRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class AndroidUserCloudDataSource(val client: HttpClient): UserCloudDataSource {
    val userBaseUrl="/user"


    override suspend fun UpdateUsername(username: String, currentUser: User?) {


        val request = UsernameUpdateRequest(currentUser?._id, currentUser?.username)

        val response: HttpResponse = client.post("$userBaseUrl/update-username") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        if (!response.status.isSuccess()) {
            throw Exception("Failed to sign in: ${response.status}")
        }


    }

    override suspend fun UpdatePhoneNumber() {
        TODO("Not yet implemented")
    }

    override fun fetchUser(): User? {
        TODO("Not yet implemented")
    }
}