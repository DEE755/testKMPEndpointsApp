package com.example.demokmpinterfacetestingapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.demokmpinterfacetestingapp.util.PickedImage
import kotlin.invoke


@Composable
expect fun rememberImagePicker(onPicked: (PickedImage?) -> Unit): () -> Unit




@Composable
fun PickImageButton(
    text: String = "Add Image",
    shape: Shape = RoundedCornerShape(8.dp),
    onImagePicked: (PickedImage) -> Unit

) {
    val launchPicker = rememberImagePicker { picked ->
        if (picked != null) onImagePicked(picked)
    }

    Box(
        Modifier.clickable { launchPicker.invoke() }.clip(shape)
    ) {

        Text(text, maxLines = 1)

    }
}


@Composable
fun PickImageButtonRectangle(
    text: String = "Add Image",
    shape: Shape = RoundedCornerShape(8.dp),
    onImagePicked: (PickedImage) -> Unit

) {
    val launchPicker = rememberImagePicker { picked ->
        if (picked != null) onImagePicked(picked)
    }

    Box(
        Modifier.clickable { launchPicker.invoke() }.clip(shape).fillMaxWidth().size(150.dp)
    ) {

        Text(text, maxLines = 1)

    }



}






@Composable
fun PickImage(
    onImagePicked: (PickedImage) -> Unit
) {
    val launchPicker = rememberImagePicker { picked ->
        if (picked != null) onImagePicked(picked)
    }
    LaunchedEffect(Unit) {
        launchPicker.invoke()
    }
    }









