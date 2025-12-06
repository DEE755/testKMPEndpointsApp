package com.example.demokmpinterfacetestingapp.ViewModel

import androidx.lifecycle.ViewModel
import com.example.demokmpinterfacetestingapp.Model.models.App
import com.example.demokmpinterfacetestingapp.Model.models.User
import com.example.demokmpinterfacetestingapp.Repository.GlobalUserRepository
import com.example.demokmpinterfacetestingapp.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AppSelectionViewModel (val userGlobalRepository: GlobalUserRepository, val sessionManager: SessionManager)
{
    val uGR : GlobalUserRepository = userGlobalRepository



    data class UiState(
        val isLoading: Boolean = false,
        val currentUser : User? = null,
        val userApps : List<App> = emptyList()
    )
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState


    private val viewModelScope by lazy {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    init {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val user = uGR.getCachedUser()
            val userApps : List<App> = uGR.getUserApps()
            _uiState.value = _uiState.value.copy(
                currentUser = user,
                userApps = userApps,
                isLoading = false
            )
        }
    }


    suspend fun getUserApps() =
        uGR.getUserApps()




    suspend fun logout()
    {
        uGR.logout()
        sessionManager.logout()

    }



}