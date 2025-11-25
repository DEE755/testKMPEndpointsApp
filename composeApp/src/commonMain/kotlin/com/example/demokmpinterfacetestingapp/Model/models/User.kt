package com.example.demokmpinterfacetestingapp.Model.models
import kotlinx.serialization.Serializable
@Serializable
data class User(val _id: String, var username: String, val email: String, val token: String, val avatarURL: String?=null, val googleUserInfo: GoogleExtraUserInfo? =null, val appleUserInfo: AppleUserInfo? = null) {
}

@Serializable
data class GoogleExtraUserInfo(
    val sub: String?=null,
    val name: String?=null,
    val given_name: String?=null,
    val family_name: String?=null,
    val picture: String?=null,
    val email_verified: Boolean,
    //Todo(add all googleinfo fields if needed)
) {

}

@Serializable
data class AppleUserInfo(val param1: String? =null){}