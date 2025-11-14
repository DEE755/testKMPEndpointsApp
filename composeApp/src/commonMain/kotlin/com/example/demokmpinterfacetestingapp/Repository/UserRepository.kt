package com.example.demokmpinterfacetestingapp.Repository

import com.example.demokmpinterfacetestingapp.Model.models.User

interface UserRepository {

    suspend fun UpdateUsername(username: String, currentUser: User?)

    suspend fun UpdatePhoneNumber()


}