package com.example.demokmpinterfacetestingapp

import com.example.demokmpinterfacetestingapp.DI.AuthTokenProvider
import com.example.demokmpinterfacetestingapp.DI.UserPrefsDataSource
import com.example.demokmpinterfacetestingapp.Model.models.User
import com.example.demokmpinterfacetestingapp.Repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SessionManager(
    private val tokenProvider: AuthTokenProvider,
    private val authRepository: AuthRepository,
    private val userPrefsDataSource: UserPrefsDataSource
) {

    data class ConnectionStatus(
        val isConnected: Boolean = false,
        val error: Exception? = null
    )
    private val _connectionStatus = MutableStateFlow(ConnectionStatus())
    val connectionStatus: StateFlow<ConnectionStatus> = _connectionStatus

    suspend fun validateToken() {
        if (!tokenProvider.hasBearerSet) {
            _connectionStatus.value = ConnectionStatus(isConnected = false)
            return
        }

        try {
            val user = authRepository.getCurrentUserFromCloud()
            _connectionStatus.value = ConnectionStatus(
                isConnected = user != null,
                error = null
            )
        } catch (e: Exception) {
            _connectionStatus.value = ConnectionStatus(
                isConnected = false,
                error = e
            )
            tokenProvider.clearAccessToken()
        }
    }

    fun setConnected(connected: Boolean) {
        _connectionStatus.value = _connectionStatus.value.copy(isConnected = connected)
    }


    fun getBearerSetStatus() =
        tokenProvider.hasBearerSet

    suspend fun logout() {
        tokenProvider.clearAccessToken()
        userPrefsDataSource.clearAllPrefs()
        _connectionStatus.value = ConnectionStatus(isConnected = false)
    }

    suspend fun getCachedUser() : User? =
        userPrefsDataSource.getCachedUser()

    suspend fun saveFullUserInCache(user: User)=
        userPrefsDataSource.saveFullUserInCache(user)


    val userFlow : kotlinx.coroutines.flow.Flow<com.example.demokmpinterfacetestingapp.Model.models.User?> = kotlinx.coroutines.flow.flowOf(null)


}