package com.example.demokmpinterfacetestingapp.com.example.demokmpinterfacetestingapp.Model.models.recycler

expect object ImageLoader {
    suspend fun fetchBytes(url: String?): ByteArray?
}