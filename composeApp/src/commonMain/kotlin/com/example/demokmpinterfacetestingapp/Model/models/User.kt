package com.example.demokmpinterfacetestingapp.Model.models
import kotlinx.serialization.Serializable
@Serializable
data class User(val _id: String, var username: String, val email: String, val token: String) {
}