package com.example.demokmpinterfacetestingapp

import kotlinx.serialization.Serializable

@Serializable
data class CodeVerificationRequest(
    val to: String,
    val code: String
)

