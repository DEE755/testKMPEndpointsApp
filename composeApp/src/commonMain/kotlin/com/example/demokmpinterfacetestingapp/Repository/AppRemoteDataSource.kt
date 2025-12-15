package com.example.demokmpinterfacetestingapp.Repository

import androidx.compose.ui.graphics.Color
import com.example.demokmpinterfacetestingapp.Model.models.App
import com.example.demokmpinterfacetestingapp.Model.models.Module

interface AppRemoteDataSource {


    suspend fun createAppRemote(
        name: String,
        modules: List<Module.Module>,
        colorHex: String,
        appLogoKey: String?,
        banner: String?=null
    ): String //version return appID instead of App object

    suspend fun createApp(name : String, modules : List<Module.Module>, color: Color, appLogoKey: String?=null, banner : String?=null): App
    suspend fun fetchUserApps(): List<App>

    suspend fun getAppByID(appID : String) : App

}