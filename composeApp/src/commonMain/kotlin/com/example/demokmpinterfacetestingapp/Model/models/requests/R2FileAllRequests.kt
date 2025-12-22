package com.example.demokmpinterfacetestingapp.Model.models.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class OwnerType { USER, APP }

@Serializable
enum class Visibility { public, private }



@Serializable
data class R2FilePresignRequest(
    @SerialName("owner_type")
    val ownerType: OwnerType,// USER or APP
    @SerialName("owner_id")
    val ownerId: String? = null,    // user id when USER; null for APP
    val folder: String,
    @SerialName("file_basename")// e.g. "assets/branding" or "users/avatars"
    val fileBasename: String? = null, // optional fixed name, else backend generates
    val mime: String,                // e.g. "image/webp", "application/pdf"
    val ext: String,                 // e.g. "webp", "pdf"
    val visibility: Visibility = Visibility.public
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