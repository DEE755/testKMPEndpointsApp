package com.example.demokmpinterfacetestingapp.Model.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class CodeVerificationRequest(
    val to: String,
    val code: String
)