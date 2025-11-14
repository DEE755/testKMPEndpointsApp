package com.example.demokmpinterfacetestingapp.Model.models.requests

import kotlinx.serialization.Serializable

@Serializable
enum class OwnerType { USER, APP }

@Serializable
enum class Visibility { PUBLIC, PRIVATE }



@Serializable
data class R2FilePresignRequest(
    val owner_type: OwnerType,       // USER or APP
    val owner_id: String? = null,    // user id when USER; null for APP
    val folder: String,         // e.g. "assets/branding" or "users/avatars"
    val file_basename: String? = null, // optional fixed name, else backend generates
    val mime: String,                // e.g. "image/webp", "application/pdf"
    val ext: String,                 // e.g. "webp", "pdf"
    val visibility: Visibility = Visibility.PUBLIC
)


@Serializable
data class R2FileCommitRequest(
    val owner_type: OwnerType,
    val owner_id: String? = null,      // null for APP
    val key: String,
    val visibility: Visibility,
    val tags: List<String> = emptyList(), // optional indexing tags
    val checksum_sha256: String? = null   // optional integrity
)


@Serializable
data class R2PrivateUrlRequest(
    val key: String,
    val ttl_seconds: Int = 300
)