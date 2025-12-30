package com.example.demokmpinterfacetestingapp.repository

import com.example.demokmpinterfacetestingapp.DI.UserPrefsDataSource
import com.example.demokmpinterfacetestingapp.Model.models.App
import com.example.demokmpinterfacetestingapp.Model.models.User
//TODO(Use this repository to manage user data from cloud and local sources, local db + prefs (cache))


class GlobalUserRepository(private val cloud : UserCloudDataSource,
                           private val local: UserLocalDataSource,
                           private val appRepository: AppRemoteDataSource,
                           private val userPrefs: UserPrefsDataSource
) {
    //val userFlow = local.userFlow

    suspend fun getCachedUser(): User ? =
        userPrefs.getCachedUser()

    suspend fun saveFullUser(user: User) =
        userPrefs.saveFullUserInCache(user)


    suspend fun logout() {
        userPrefs.clearAllPrefs()
    }

    suspend fun getUserApps() : List<App> =
        appRepository.fetchUserApps()


}

