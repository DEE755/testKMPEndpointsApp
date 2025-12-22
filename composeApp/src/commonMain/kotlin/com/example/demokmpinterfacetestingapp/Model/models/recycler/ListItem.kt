package com.example.demokmpinterfacetestingapp.Model.models.recycler

import androidx.compose.ui.graphics.Color

data class ListItem(
    val id: String,
    val title: String,
    val thumbnailUrl: String?,
    val color: String =Color.LightGray.value.toString()
)