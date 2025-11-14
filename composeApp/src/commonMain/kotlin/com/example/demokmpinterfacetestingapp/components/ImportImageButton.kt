package com.example.demokmpinterfacetestingapp.com.example.demokmpinterfacetestingapp.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow

data class PickedImage(
    val name: String?,
    val mimeType: String?,
    val bytes: ByteArray
)

@Composable
expect fun rememberImagePicker(onPicked: (PickedImage?) -> Unit): () -> Unit


@Composable
fun UploadImageButton(
    onImage: (PickedImage) -> Unit
) {
    val launchPicker = rememberImagePicker { picked ->
        if (picked != null) onImage(picked)
    }
    Button(onClick = launchPicker) {
        Text("Upload picture", maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}