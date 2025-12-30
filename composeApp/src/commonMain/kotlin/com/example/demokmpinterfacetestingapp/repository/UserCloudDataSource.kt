package com.example.demokmpinterfacetestingapp.repository

import com.example.demokmpinterfacetestingapp.Model.models.User

interface UserCloudDataSource {

    suspend fun UpdateUsername(username: String, currentUser: User?)

    suspend fun UpdatePhoneNumber()


    fun fetchUser() : User?


}