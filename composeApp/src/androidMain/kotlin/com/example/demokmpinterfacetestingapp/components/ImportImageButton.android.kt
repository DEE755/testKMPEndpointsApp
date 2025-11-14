package com.example.demokmpinterfacetestingapp.com.example.demokmpinterfacetestingapp.components

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.demokmpinterfacetestingapp.util.PickedImage

@Composable
actual fun rememberImagePicker(onPicked: (PickedImage?) -> Unit): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) {
            onPicked(null)
            return@rememberLauncherForActivityResult
        }
        val mime = context.contentResolver.getType(uri)
        val name = queryDisplayName(context, uri)
        val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
        onPicked(bytes?.let { PickedImage(name, mime, it) })
    }
    return { launcher.launch("image/*") }
}


private fun queryDisplayName(context: Context, uri: Uri): String? {
    val projection = arrayOf(OpenableColumns.DISPLAY_NAME)
    context.contentResolver.query(uri, projection, null, null, null)?.use { c ->
        if (c.moveToFirst()) {
            val idx = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (idx != -1) return c.getString(idx)
        }
    }
    return null
}