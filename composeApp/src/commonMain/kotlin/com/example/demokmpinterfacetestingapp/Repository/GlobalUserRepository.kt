package com.example.demokmpinterfacetestingapp.Repository

import com.example.demokmpinterfacetestingapp.DI.AuthTokenProvider
import com.example.demokmpinterfacetestingapp.Model.models.App
import com.example.demokmpinterfacetestingapp.Model.models.User
//TODO(Use this repository to manage user data from cloud and local sources, local db + prefs (cache))


class GlobalUserRepository( private val cloud : UserCloudDataSource,
private val local: UserLocalDataSource,
    private val appRepository: AppRepository
) {
    val userFlow = local.userFlow

    suspend fun getCachedUser(): User ? =
        local.getCachedUser()

    suspend fun saveFullUser(user: User) =
        local.saveFullUserInCache(user)


    suspend fun logout() {
        local.logout()
    }

    suspend fun getUserApps() : List<App> =
        appRepository.fetchUserApps()


}

