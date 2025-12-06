package com.example.demokmpinterfacetestingapp.Model.models.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AppCreationResponse(
    val success: Boolean,
    val message: String,
    @SerialName("app_id")
    val appId: String
)

