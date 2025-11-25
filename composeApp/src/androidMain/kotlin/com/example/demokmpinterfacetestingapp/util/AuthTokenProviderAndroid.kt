package com.example.demokmpinterfacetestingapp.util

import android.content.Context
import android.content.SharedPreferences
import com.example.demokmpinterfacetestingapp.com.example.demokmpinterfacetestingapp.AuthTokenProvider
import androidx.core.content.edit

class AuthTokenProviderAndroid(private val context: Context) : AuthTokenProvider
{
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    }

    override suspend fun getAccessToken() : String? =
        prefs.getString("access_token", null)


    override suspend fun saveAccessToken(token: String) {
        prefs.edit { putString("access_token", token) }
    }

    override suspend fun clearAccessToken() {prefs.edit {clear() }}

    override var hasBearerSet: Boolean = !prefs.getString("access_token", null).isNullOrBlank()
}