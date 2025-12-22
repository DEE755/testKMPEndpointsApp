package com.example.demokmpinterfacetestingapp.Repository

import androidx.compose.ui.graphics.ImageBitmap
import com.example.demokmpinterfacetestingapp.Model.models.requests.Visibility
import com.example.demokmpinterfacetestingapp.Model.models.responses.R2CommitResponse
import com.example.demokmpinterfacetestingapp.Model.models.responses.R2FilePresignResponse

interface CloudFilesRepository {
    suspend fun presignAppFileUpload(
        folder: String,
        fileBasename: String?,
        mime: String,
        ext: String,
        visibility: Visibility = Visibility.public
    ): R2FilePresignResponse

    suspend fun presignUserFileUpload(
        ownerId: String,
        folder: String,
        fileBasename: String?,
        mime: String,
        ext: String,
        visibility: Visibility = Visibility.private
    ): R2FilePresignResponse

    suspend fun fetchFile(URI: String): ImageBitmap

    suspend fun commitAppFile(key: String, tags: List<String> = emptyList(), checksum: String? = null)

    suspend fun commitUserFile(owner_id: String, key: String, tags: List<String> = emptyList(), checksum: String? = null) : R2CommitResponse


    suspend fun uploadToR2(presignedUrl: String, fileBytes: ByteArray, contentType: String): Boolean

    suspend fun getFilesListFromCloudDB(folder: String): List<String>

}
