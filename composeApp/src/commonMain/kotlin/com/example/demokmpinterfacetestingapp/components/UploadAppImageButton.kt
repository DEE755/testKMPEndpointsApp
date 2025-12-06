package com.example.demokmpinterfacetestingapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text

import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.demokmpinterfacetestingapp.util.PickedImage
import com.example.demokmpinterfacetestingapp.util.decodeImage


/*
var selected by remember { mutableStateOf<PickedImage?>(null) }

Row {
    Column(Modifier.padding(16.dp)) {
        PickImageButton { img -> selected = img }


        Spacer(Modifier.height(12.dp))
        if (selected != null) {
            val bmp = remember(selected) { decodeImage(selected!!.bytes) }
            Image(bitmap = bmp, contentDescription = "Selected image", modifier = Modifier.size(120.dp))
            Text("Nom: ${selected!!.name ?: "-"}")
            Text("MIME: ${selected!!.mimeType ?: "-"}")
            Text("Taille: ${selected!!.bytes.size} octets")
        }
    }



Button(
onClick = {
    selected?.let { image ->
        viewModel.uploadUserImage(
            image = image,
            folder = "app-images",
            fileBasename = "app-logo"
        )
    }
},
enabled = selected != null
) {
    Text("Upload to server")
}
}*/
