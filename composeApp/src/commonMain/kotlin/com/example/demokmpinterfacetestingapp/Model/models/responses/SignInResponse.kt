package com.example.demokmpinterfacetestingapp.com.example.demokmpinterfacetestingapp.Model.models.responses

import com.example.demokmpinterfacetestingapp.Model.models.User
import kotlinx.serialization.Serializable

@Serializable
data class SignInResponse(val user: User, val status: Int) {

}


@Serializable
data class GoogleSignInResponse(val user_status: String? =null, val token: String? =null, val username: String? =null, val user_id: String? =null, val email: String? =null, val google_avatar_url: String? =null, val email_verified: Boolean? =null, val errorMessage: String? = null) {

}


@Serializable
data class CurrentUserResponse(val user: User? = null, val status: Int? = null)


