package com.example.demokmpinterfacetestingapp.Model.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class MobilePhoneRequest(val mobile_number: String)