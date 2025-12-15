package com.example.demokmpinterfacetestingapp.Repository

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.ui.graphics.Color
import com.example.demokmpinterfacetestingapp.Model.models.App
import com.example.demokmpinterfacetestingapp.Model.models.Module

interface AppRepository {
    suspend fun createApp(
        name: String,
        modules: List<Module.Module>,
        color: Color,
        appLogoKey: String?,
        banner: String?=null
    ): App

    suspend fun getAppByID(appID: String): App
    suspend fun fetchUserApps(): List<App>

    val apps: StateFlow<List<App>>
    val newAppEvents: SharedFlow<App>

    val isLoading: StateFlow<Boolean>
    fun setLoading(boolean: Boolean)

    fun addFullList(appsList: List<App>)
}