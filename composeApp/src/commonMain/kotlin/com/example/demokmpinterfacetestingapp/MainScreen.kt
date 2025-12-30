package com.example.demokmpinterfacetestingapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import app.AppRoot
import com.example.demokmpinterfacetestingapp.components.GeneralDrawerScreen


@Composable
fun MainScreen() {

    Column {

        GeneralDrawerScreen(
            {
                Column {
                Spacer(Modifier.height(90.dp))
                AppRoot() }
            }
        )
    }

}
