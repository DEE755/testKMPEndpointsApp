package com.example.demokmpinterfacetestingapp.Repository

import androidx.compose.ui.graphics.ImageBitmap
import com.example.demokmpinterfacetestingapp.Model.models.requests.Visibility
import com.example.demokmpinterfacetestingapp.Model.models.responses.R2FilePresignResponse
import com.example.demokmpinterfacetestingapp.com.example.demokmpinterfacetestingapp.Model.models.responses.GoogleSignInResponse

interface CloudFilesRepository {
    suspend fun presignAppFileUpload(
        folder: String,
        file_basename: String?,
        mime: String,
        ext: String,
        visibility: Visibility = Visibility.PUBLIC
    ): R2FilePresignResponse

    suspend fun presignUserFileUpload(
        owner_id: String,
        folder: String,
        file_basename: String?,
        mime: String,
        ext: String,
        visibility: Visibility = Visibility.PRIVATE
    ): R2FilePresignResponse

    suspend fun fetchFile(URI: String): ImageBitmap

    suspend fun commitAppFile(key: String, tags: List<String> = emptyList(), checksum: String? = null)

    suspend fun commitUserFile(owner_id: String, key: String, tags: List<String> = emptyList(), checksum: String? = null)


    suspend fun uploadToR2(presignedUrl: String, fileBytes: ByteArray, contentType: String): Boolean

    suspend fun getFilesListFromCloudDB(folder: String): List<String>

}