package com.example.demokmpinterfacetestingapp.Model.models.responses

import com.example.demokmpinterfacetestingapp.Model.models.User
import kotlinx.serialization.Serializable

@Serializable
data class SignInResponse(val user: User, val status: Int) {

}