package com.example.demokmpinterfacetestingapp.Repository

import com.example.demokmpinterfacetestingapp.DI.UserPrefsDataSource
import com.example.demokmpinterfacetestingapp.Model.models.User

class AndroidLocalUserDataSource (val userPrefs : UserPrefsDataSource) : UserLocalDataSource {


    override suspend fun getCachedUser(): User? =
        userPrefs.getCachedUser()


    override suspend fun saveFullUserInCache(user: User) =
       userPrefs.saveFullUserInCache(user)


    override suspend fun logout() {
        userPrefs.clearAllPrefs()
    }




    override val userFlow: kotlinx.coroutines.flow.Flow<com.example.demokmpinterfacetestingapp.Model.models.User?> = kotlinx.coroutines.flow.flowOf(null)
}