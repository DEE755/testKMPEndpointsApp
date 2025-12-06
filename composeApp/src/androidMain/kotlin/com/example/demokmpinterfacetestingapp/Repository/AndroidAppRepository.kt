package com.example.demokmpinterfacetestingapp.Repository

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.demokmpinterfacetestingapp.Const.CustomApiParams
import com.example.demokmpinterfacetestingapp.Model.models.App
import com.example.demokmpinterfacetestingapp.Model.models.Module
import com.example.demokmpinterfacetestingapp.Model.models.requests.AppCreationRequest
import com.example.demokmpinterfacetestingapp.Model.models.responses.AppCreationResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class AndroidAppRepository(val client : HttpClient) : AppRepository {
    private val appBaseUrl= CustomApiParams.getBaseUrl() + "/apps"

        override suspend fun createApp(
            name: String,
            modules: List<Module.Module>,
            color: Color,
            appLogoKey: String?,
            banner : String?
        ): App {

            val colorHex = String.format("#%08X", color.toArgb())


            val request = AppCreationRequest(
                name = name,
                modules = modules,
                color = colorHex,
                appLogoKey = appLogoKey,
                banner = null
            )

            val response = client.post("$appBaseUrl/create") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (!response.status.isSuccess()) {
                throw Exception("Failed to create app: ${response.status}")
            }

            val body : AppCreationResponse = response.body()

            val newAppId = body.appId

            val appInserted = getAppByID(newAppId)


            return appInserted
        }

    override suspend fun getAppByID(appID: String): App {
        val response : HttpResponse = client.get("$appBaseUrl/$appID") {
            contentType(ContentType.Application.Json)
        }
        if (!response.status.isSuccess()) {
            throw Exception("Failed to get app by ID: ${response.status}")
        }
        val parsedApp = response.body<App>()
        return parsedApp
    }




    override suspend fun fetchUserApps(): List<App> {
        val response: HttpResponse = client.get("$appBaseUrl/fetch_user_apps") {
            contentType(ContentType.Application.Json)
        }
        if (!response.status.isSuccess()) {
            throw Exception("Failed to fetch user apps: ${response.status}")
        }
        val parsed = response.body<List<App>>()
        return parsed
    }


}