package com.example.demokmpinterfacetestingapp.DI;

import android.content.Context;
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.demokmpinterfacetestingapp.Model.models.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AndroidUserPrefsDataSource(private val context:Context) : UserPrefsDataSource
{

    private val userPrefs: SharedPreferences by lazy {
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }


    override suspend fun getUserId(): String? =
        userPrefs.getString("user_id", null)



    override suspend fun saveUserId(userId: String?) =
            userPrefs.edit { putString("user_id", userId) }


    override suspend fun getUsername(): String? =
        userPrefs.getString("username", null)


    override suspend fun saveUsername(username: String?) =
        userPrefs.edit { putString("username", username) }

    override suspend fun getGivenName(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun saveGivenName(givenName: String?) {
        TODO("Not yet implemented")
    }

    override suspend fun saveFullUserInCache(user: User) {
        val jsonUser = Json.encodeToString(user)
        userPrefs.edit { putString("current_user", jsonUser)}
    }

    override suspend fun saveFullGoogleUserInCache(user: User) {
        val jsonUser = Json.encodeToString(user)
        userPrefs.edit { putString("current_google_user", jsonUser)}
    }

    override suspend fun getCachedGoogleUser(): User? {
        val jsonUser = userPrefs.getString("current_google_user", null)

        return try {
            Json.decodeFromString<User>(jsonUser!!)
        } catch (e: Exception) {
            null
        }

    }

    override suspend fun getCachedUser() : User? {
        val jsonUser = userPrefs.getString("current_user", null)

        return try {
            Json.decodeFromString<User>(jsonUser!!)
        } catch (e: Exception) {
            null
        }

    }


    override suspend fun saveUserEmail(email: String?) =
        userPrefs.edit { putString("user_email", email) }


    override suspend fun getUserEmail(): String? =
        userPrefs.getString("user_email", null)



    override suspend fun clearAllPrefs() {
        userPrefs.edit { clear() }
    }

}
