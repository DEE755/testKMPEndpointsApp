package com.example.demokmpinterfacetestingapp.Model.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class EmailSignInRequest(val email: String, val password: String) {
}