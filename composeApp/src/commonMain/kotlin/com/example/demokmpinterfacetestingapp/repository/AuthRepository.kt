package com.example.demokmpinterfacetestingapp.repository

import com.example.demokmpinterfacetestingapp.Model.models.User

interface AuthRepository {
    //SMS Auth
    suspend fun sendMobilePhone(phone: String): String
    suspend fun verifyCode(to: String, code: String): String

    //Email Auth
     suspend fun emailSignIn(email: String, password : String): User?

    suspend fun emailSignUp(email: String, password : String, username: String) : User?


    suspend fun getCurrentUserFromCloud(): User?

    suspend fun googleSignIn(idToken: String, nonce: String?): User?

    suspend fun saveAccessToken(token: String?)
}