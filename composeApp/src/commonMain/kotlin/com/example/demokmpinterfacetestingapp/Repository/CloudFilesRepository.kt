package com.example.demokmpinterfacetestingapp.com.example.demokmpinterfacetestingapp.Repository

import com.example.demokmpinterfacetestingapp.com.example.demokmpinterfacetestingapp.Model.models.requests.R2FilePresignRequest
import com.example.demokmpinterfacetestingapp.com.example.demokmpinterfacetestingapp.Model.models.requests.Visibility
import com.example.demokmpinterfacetestingapp.com.example.demokmpinterfacetestingapp.Model.models.responses.R2FilePresignResponse

interface CloudFilesRepository
{
    suspend fun presignAppFileUpload(folder_path: String, fileBaseName: String?, mime: String, ext: String, visibility: Visibility = Visibility.PUBLIC): R2FilePresignResponse

    suspend fun presignUserFileUpload(user_id: String, folder_path: String, fileBaseName: String?, mime: String, ext: String, visibility: Visibility = Visibility.PRIVATE): R2FilePresignResponse

    suspend fun fetchFile(URI: String)

    suspend fun commitAppFile(key: String, tags : List<String> = emptyList(), checksum: String? = null)

    suspend fun commitUserFile(user_id: String, key: String, tags : List<String> = emptyList(), checksum: String? = null)
}