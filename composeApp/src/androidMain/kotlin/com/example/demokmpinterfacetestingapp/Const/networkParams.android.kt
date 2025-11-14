package com.example.demokmpinterfacetestingapp.Const

actual class CustomApiParams {

    actual companion object {
        actual fun getBaseUrl(): String {
            return "http://10.0.2.2:7001"
        }
    }
}