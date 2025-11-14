package com.example.demokmpinterfacetestingapp.Repository

import com.example.demokmpinterfacetestingapp.com.example.demokmpinterfacetestingapp.Model.models.requests.OwnerType
import com.example.demokmpinterfacetestingapp.com.example.demokmpinterfacetestingapp.Model.models.requests.R2FileCommitRequest
import com.example.demokmpinterfacetestingapp.com.example.demokmpinterfacetestingapp.Model.models.requests.R2FilePresignRequest
import com.example.demokmpinterfacetestingapp.com.example.demokmpinterfacetestingapp.Model.models.requests.Visibility
import com.example.demokmpinterfacetestingapp.com.example.demokmpinterfacetestingapp.Model.models.responses.R2FilePresignResponse
import com.example.demokmpinterfacetestingapp.com.example.demokmpinterfacetestingapp.Repository.CloudFilesRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess


class CloudFilesRepositoryAndroid(val client: HttpClient) : CloudFilesRepository {
    val fileBaseUrl = "/files/"


    override suspend fun presignAppFileUpload(
        folder_path: String,
        file_basename: String?,
        mime: String,
        ext: String,
        visibility: Visibility
    ): R2FilePresignResponse {
        val request = R2FilePresignRequest(OwnerType.APP, null, folder_path, file_basename, mime, ext, Visibility.PUBLIC)
        val response: HttpResponse = client.post("$fileBaseUrl/presign") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        if (!response.status.isSuccess()) {
            throw Exception("Failed to presign file upload: ${response.status} - ${response.bodyAsText()}")
        }

        return response.body()
    }

    override suspend fun presignUserFileUpload(
        user_id: String,
        folder_path: String,
        file_basename: String?,
        mime: String,
        ext: String,
        visibility: Visibility
    ): R2FilePresignResponse {
        val request =
            R2FilePresignRequest(OwnerType.USER, user_id, folder_path, file_basename, mime, ext, Visibility.PRIVATE)
        val response: HttpResponse = client.post("$fileBaseUrl/presign") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        if (!response.status.isSuccess()) {
            throw Exception("Failed to presign file upload: ${response.status} - ${response.bodyAsText()}")
        }

        return response.body()
    }


    override suspend fun fetchFile(URI: String) {
        TODO("Not yet implemented")
    }


    override suspend fun commitAppFile(key: String, tags: List<String>, checksum: String?) {

        val request = R2FileCommitRequest(OwnerType.APP, null, key, Visibility.PUBLIC, tags)

        val response: HttpResponse = client.post("$fileBaseUrl/commit") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        if (!response.status.isSuccess()) {
            throw Exception("Failed to commit file upload: ${response.status}")
        }


    }


    override suspend fun commitUserFile(user_id: String, key: String, tags: List<String>, checksum: String?) {
        val request = R2FileCommitRequest(OwnerType.USER, user_id, key, Visibility.PRIVATE, tags)

        val response: HttpResponse = client.post("$fileBaseUrl/commit") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        if (!response.status.isSuccess()) {
            throw Exception("Failed to presign file upload: ${response.status}")
        }


    }

}