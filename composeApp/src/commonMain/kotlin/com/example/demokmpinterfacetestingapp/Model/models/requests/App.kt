package com.example.demokmpinterfacetestingapp.Model.models.requests

import com.example.demokmpinterfacetestingapp.Model.models.Module
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppCreationRequest(
    val name:String,
    val modules: List<Module.Module>,
    val color: String,
    @SerialName("app_logo_key")
    val appLogoKey: String?= null,
    val banner : String? = null
){
}