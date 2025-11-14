package com.example.demokmpinterfacetestingapp.com.example.demokmpinterfacetestingapp.Model.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class R2PrivateUrlRequest(
    val key: String,
    val ttl_seconds: Int = 300
)


@Serializable
data class R2FilePresignResponse(
    val key: String,           // final object key chosen by backend
    val url: String,           // presigned POST endpoint
    val fields: Map<String, String>, // form fields to include in POST
    val expires_in: Int
)