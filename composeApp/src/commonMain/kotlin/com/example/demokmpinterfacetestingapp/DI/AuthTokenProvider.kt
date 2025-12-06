package com.example.demokmpinterfacetestingapp.DI

interface AuthTokenProvider {


    suspend fun getAccessToken(): String?

    suspend fun saveAccessToken(token: String?)

    suspend fun clearAccessToken()

    var hasBearerSet: Boolean

    suspend fun setHasBearerSet(value: Boolean)
    suspend fun getHasBearerSet(): Boolean
}