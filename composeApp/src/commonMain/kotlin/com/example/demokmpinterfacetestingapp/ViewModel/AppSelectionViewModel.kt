package com.example.demokmpinterfacetestingapp.ViewModel

import com.example.demokmpinterfacetestingapp.Model.models.App
import com.example.demokmpinterfacetestingapp.Model.models.User
import com.example.demokmpinterfacetestingapp.Repository.AppRepository
import com.example.demokmpinterfacetestingapp.Repository.GlobalUserRepository
import com.example.demokmpinterfacetestingapp.SessionManager
import com.example.demokmpinterfacetestingapp.ui.showToast
import io.ktor.utils.io.core.Closeable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AppSelectionViewModel (val userGlobalRepository: GlobalUserRepository,
                             val appRepo : AppRepository,
                             val sessionManager: SessionManager) : Closeable
{
    val uGR : GlobalUserRepository = userGlobalRepository


    data class UiState(
        val isLoading: Boolean = false,
        val currentUser : User? = null,
        val userApps : List<App> = emptyList()
    ) {

    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState


    private val viewModelScope by lazy {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    init {
        viewModelScope.launch {
            sessionManager.connectionStatus.collect {
                if (it.isConnected) {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                    val user = uGR.getCachedUser()
                   // val userApps: List<App> = uGR.getUserApps()
                    _uiState.value = _uiState.value.copy(
                        currentUser = user,
                       // userApps = userApps
                    )
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }

                // Todo("else unregistered user handling", local/special guest token fetch)
            }
        }
    }


    fun updateUserApps() {
        viewModelScope.launch {
    val userApps: List<App> = uGR.getUserApps()
        _uiState.value = _uiState.value.copy( userApps = userApps )
        appRepo.addFullList(userApps)
    }
        }




    fun setLoading(bool: Boolean) {
        _uiState.value = _uiState.value.copy( isLoading = bool)

    }


    fun getAllAppsList(): StateFlow<List<App>> {
        return appRepo.apps
    }

    fun logout()
    { viewModelScope.launch {
        uGR.logout()
        sessionManager.logout()
        close()
    }
    }

    override fun close() {
        viewModelScope.cancel()
        _uiState.value=UiState()
    }

    fun showToast(msg: String) {
        viewModelScope.launch {showToast(msg)}
    }

}