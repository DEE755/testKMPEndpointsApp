package com.example.demokmpinterfacetestingapp.Model.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class UsernameUpdateRequest(val _id: String?, val username: String?) {
}