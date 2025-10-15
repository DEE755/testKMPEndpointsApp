package com.example.demokmpinterfacetestingapp

interface NetworkRepository {
    suspend fun sendMobilePhone(phone: String): String
}

