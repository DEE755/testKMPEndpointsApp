package com.example.demokmpinterfacetestingapp.Repository

import com.example.demokmpinterfacetestingapp.Model.models.User

interface UserLocalDataSource {
    suspend fun getCachedUser() : User?
    suspend fun saveFullUserInCache(user: User)

    suspend fun logout()

    val userFlow : kotlinx.coroutines.flow.Flow<com.example.demokmpinterfacetestingapp.Model.models.User?>
}