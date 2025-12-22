package com.example.demokmpinterfacetestingapp.Model.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
class App(
    val _id: String,
    val name:String,
    @SerialName("owner_id")
    val ownerId: String,
    val modules: List<Module.Module>,
    val color: String,
    @SerialName("app_icon_url")
    var appIconURL: String?= null
){
}




