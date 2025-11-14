package com.example.demokmpinterfacetestingapp.Model.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class EmailSignUpRequest(val email: String, val password: String, val username: String) {
}