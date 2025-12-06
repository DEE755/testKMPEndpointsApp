package com.example.demokmpinterfacetestingapp.util

import android.content.Context
import android.content.SharedPreferences
import com.example.demokmpinterfacetestingapp.DI.AuthTokenProvider
import androidx.core.content.edit
import com.example.demokmpinterfacetestingapp.DI.ServiceLocator.tokenProvider

class AuthTokenProviderAndroid(private val context: Context) : AuthTokenProvider
{
    private val authPrefs: SharedPreferences by lazy {
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    }

    override suspend fun getAccessToken() : String? =
        authPrefs.getString("access_token", null)


    override suspend fun saveAccessToken(token: String?) {
        authPrefs.edit { putString("access_token", token) }
        tokenProvider.hasBearerSet = true
    }

    override suspend fun clearAccessToken() {authPrefs.edit {clear() }}

    override suspend fun setHasBearerSet(value: Boolean) {
        hasBearerSet = value
    }

    override suspend fun getHasBearerSet(): Boolean =
        hasBearerSet

    override var hasBearerSet: Boolean = !authPrefs.getString("access_token", null).isNullOrBlank()
}