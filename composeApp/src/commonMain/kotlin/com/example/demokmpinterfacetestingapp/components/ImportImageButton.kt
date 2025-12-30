package com.example.demokmpinterfacetestingapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.example.demokmpinterfacetestingapp.util.PickedImage


@Composable
expect fun rememberImagePicker(onPicked: (PickedImage?) -> Unit): () -> Unit




@Composable
fun PickImageButton(shape: Shape = CircleShape,
                    onImagePicked: (PickedImage) -> Unit
) {
    val launchPicker = rememberImagePicker { picked ->
        if (picked != null) onImagePicked(picked)
    }

    VectorialPlusButton(shape=shape,onClick = {launchPicker.invoke()})
}


@Composable
fun PickImageRectangleButton(shape: Shape = CircleShape,
                    onImagePicked: (PickedImage) -> Unit
) {
    val launchPicker = rememberImagePicker { picked ->
        if (picked != null) onImagePicked(picked)
    }

    VectorialBannerPlusButton(shape=shape,onClick = {launchPicker.invoke()})
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
        Modifier.clickable { launchPicker.invoke() }.clip(shape).fillMaxWidth().height(150.dp)
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









