package com.example.demokmpinterfacetestingapp.util

import androidx.compose.ui.graphics.ImageBitmap



data class PickedImage(
    val name: String?,
    val mimeType: String?,
    val bytes: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PickedImage

        if (name != other.name) return false
        if (mimeType != other.mimeType) return false
        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }


    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (mimeType?.hashCode() ?: 0)
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}

expect fun decodeImage(bytes: ByteArray): ImageBitmap