package com.example.demokmpinterfacetestingapp.DI

import com.example.demokmpinterfacetestingapp.Model.models.User

interface UserPrefsDataSource {

    suspend fun getUserId(): String?
    suspend fun saveUserId(userId: String?)

    suspend fun getUsername(): String?
    suspend fun saveUsername(username: String?)

    suspend fun getGivenName(): String?
    suspend fun saveGivenName(givenName: String?)


    suspend fun saveFullUserInCache(user: User)
    suspend fun getCachedUser(): User?

    suspend fun saveUserEmail(email: String?)
    suspend fun getUserEmail(): String?
    suspend fun clearAllPrefs()
    suspend fun saveFullGoogleUserInCache(user: User)
    suspend fun getCachedGoogleUser(): User?
}