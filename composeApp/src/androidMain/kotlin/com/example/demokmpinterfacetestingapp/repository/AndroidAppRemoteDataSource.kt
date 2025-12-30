package com.example.demokmpinterfacetestingapp.repository

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.demokmpinterfacetestingapp.constants.CustomApiParams
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

class AndroidAppRemoteDataSource(val client : HttpClient) : AppRemoteDataSource {


    private val appBaseUrl= CustomApiParams.getBaseUrl() + "/apps"

    override suspend fun createAppRemote(
        name: String,
        modules: List<Module.Module>,
        colorHex: String,
        appLogoKey: String?,
        banner: String?
    ): String {
        val request = mapOf(
            "name" to name,
            "modules" to modules,
            "color" to colorHex,
            "appLogoKey" to appLogoKey,
            "banner" to banner
        )
        val response: HttpResponse = client.post("$appBaseUrl/create") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        if (!response.status.isSuccess()) throw Exception("Failed to create")
        val body = response.body<Map<String, Any>>()
        return body["appId"].toString()
    }

        override suspend fun createApp(
            name: String,
            modules: List<Module.Module>,
            color: Color,
            appLogoKey: String?,
            appBannerKey : String?
        ): App {

            val colorHex = String.format("#%08X", color.toArgb())


            val request = AppCreationRequest(
                name = name,
                modules = modules,
                color = colorHex,
                appLogoKey = appLogoKey,
                banner = appBannerKey
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