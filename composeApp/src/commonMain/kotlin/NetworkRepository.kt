package com.example.demokmpinterfacetestingapp

interface NetworkRepository {
    suspend fun sendMobilePhone(phone: String): String
    suspend fun verifyCode(to: String, code: String): String
}
