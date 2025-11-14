package com.example.demokmpinterfacetestingapp.Repository

import com.example.demokmpinterfacetestingapp.Model.models.User

interface AuthRepository {
    //SMS Auth
    suspend fun sendMobilePhone(phone: String): String
    suspend fun verifyCode(to: String, code: String): String

    //Email Auth
     suspend fun emailSignIn(email: String, password : String): User?

    suspend fun emailSignUp(email: String, password : String, username: String)






}