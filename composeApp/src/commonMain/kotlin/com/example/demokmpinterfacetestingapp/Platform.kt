package com.example.demokmpinterfacetestingapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform