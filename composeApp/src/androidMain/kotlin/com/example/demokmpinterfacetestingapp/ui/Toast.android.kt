package com.example.demokmpinterfacetestingapp.ui

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import di.ServiceLocator.appContextInstance




actual suspend fun showToast(message: String) {
    withContext(Dispatchers.Main) {
        Toast.makeText(appContextInstance as Context, message, Toast.LENGTH_SHORT).show()
    }
}