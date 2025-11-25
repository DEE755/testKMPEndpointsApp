package com.example.demokmpinterfacetestingapp.com.example.demokmpinterfacetestingapp

interface AuthTokenProvider {


    suspend fun getAccessToken(): String?

    suspend fun saveAccessToken(token: String)

    suspend fun clearAccessToken()

    var hasBearerSet: Boolean

}