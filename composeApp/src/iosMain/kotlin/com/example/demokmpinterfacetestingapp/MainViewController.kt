package com.example.demokmpinterfacetestingapp

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController { MainScreen(NetworkRepositoryImpl()) }
