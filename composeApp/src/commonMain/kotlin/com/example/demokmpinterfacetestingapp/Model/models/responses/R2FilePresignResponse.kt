package com.example.demokmpinterfacetestingapp.Model.models.responses

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
    val expires_in: Int
)

@Serializable
data class R2CommitResponse(
    val ok: Boolean,
    val key: String,
    val file_id: String
)
