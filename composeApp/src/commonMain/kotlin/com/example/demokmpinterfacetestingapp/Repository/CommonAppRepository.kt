package com.example.demokmpinterfacetestingapp.Repository

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.demokmpinterfacetestingapp.Model.models.App
import com.example.demokmpinterfacetestingapp.Model.models.Module
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CommonAppRepository(
    private val remote: AppRemoteDataSource
) : AppRepository {

    private val _apps = MutableStateFlow<List<App>>(emptyList())
    override val apps: StateFlow<List<App>> = _apps

    private val _newAppEvents = MutableSharedFlow<App>(extraBufferCapacity = 1)
    override val newAppEvents: SharedFlow<App> = _newAppEvents

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = _isLoading

    override fun setLoading(boolean: Boolean) {
        _isLoading.update { boolean }
    }

    override suspend fun createApp(
        name: String,
        modules: List<Module.Module>,
        color: Color,
        appLogoKey: String?,
        banner: String?
    ): App {
        val colorHex = "#" + ((color.toArgb().toLong() and 0xFFFFFFFFL).toString(16).uppercase().padStart(8, '0'))
        //val newId = remote.createApp(name, modules, colorHex, appLogoKey, banner)
        val created = remote.createApp(name, modules, color, appLogoKey, banner)
        //val created = remote.getAppByID(newId)
        println("Create app: $created")
        onAppInserted(created)
        return created
    }

    override suspend fun getAppByID(appID: String): App {
        return remote.getAppByID(appID)
    }

    override suspend fun fetchUserApps(): List<App> {
        val list = remote.fetchUserApps()
        _apps.value = list
        return list
    }

    private fun onAppInserted(app: App) {
        _apps.update { current -> current + app }
        _newAppEvents.tryEmit(app)
    }


    override fun addFullList(appsList: List<App>) {
        _apps.value = appsList
    }

}