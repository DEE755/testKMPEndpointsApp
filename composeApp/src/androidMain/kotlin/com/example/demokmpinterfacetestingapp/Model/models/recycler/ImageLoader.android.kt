package com.example.demokmpinterfacetestingapp.com.example.demokmpinterfacetestingapp.Model.models.recycler

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

actual object ImageLoader {
    actual suspend fun fetchBytes(url: String?): ByteArray? {
        if (url.isNullOrBlank()) return null
        return withContext(Dispatchers.IO) {
            try {
                URL(url).openStream().use { it.readBytes() }
            } catch (e: Exception) {
                null
            }
        }
    }
}