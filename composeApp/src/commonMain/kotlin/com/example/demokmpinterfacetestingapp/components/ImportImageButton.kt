package com.example.demokmpinterfacetestingapp.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import com.example.demokmpinterfacetestingapp.util.PickedImage


@Composable
expect fun rememberImagePicker(onPicked: (PickedImage?) -> Unit): () -> Unit




@Composable
fun UploadImageButton(
    onImagePicked: (PickedImage) -> Unit
) {
    val launchPicker = rememberImagePicker { picked ->
        if (picked != null) onImagePicked(picked)
    }
    Button(onClick = launchPicker) {
        Text("Choose Picture to upload", maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}







