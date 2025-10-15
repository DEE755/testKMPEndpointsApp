package com.example.demokmpinterfacetestingapp

import kotlinx.serialization.Serializable

@Serializable
data class MobilePhoneRequest(val mobile_number: String)
