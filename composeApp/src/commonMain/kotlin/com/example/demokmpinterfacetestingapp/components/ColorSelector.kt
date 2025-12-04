package com.example.demokmpinterfacetestingapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val defaultColors = listOf(
    Color(0xFFEF476F),
    Color(0xFFFFA62B),
    Color(0xFFFFD166),
    Color(0xFF06D6A0),
    Color(0xFF118AB2),
    Color(0xFF073B4C)
)

@Composable
fun ColorSelector(
    colors: List<Color> = defaultColors,
    selectedColor: Color?,
    onColorSelected: (Color) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        colors.forEach { c ->
            ColorSwatch(color = c, selected = (c == selectedColor)) { onColorSelected(c) }
        }
    }
}


@Composable
fun ColorSwatch(color: Color, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .padding(4.dp)
            .background(color = color, shape = RoundedCornerShape(6.dp))
            .border(
                width = if (selected) 3.dp else 1.dp,
                color = if (selected) Color.Black else Color.Gray,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable(onClick = onClick)
    )
}