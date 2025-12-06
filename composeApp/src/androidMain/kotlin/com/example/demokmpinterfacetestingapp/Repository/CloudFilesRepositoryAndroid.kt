package com.example.demokmpinterfacetestingapp.Repository

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.example.demokmpinterfacetestingapp.Const.CustomApiParams
import com.example.demokmpinterfacetestingapp.Model.models.requests.OwnerType
import com.example.demokmpinterfacetestingapp.Model.models.requests.R2FileCommitRequest
import com.example.demokmpinterfacetestingapp.Model.models.requests.R2FilePresignRequest
import com.example.demokmpinterfacetestingapp.Model.models.requests.Visibility
import com.example.demokmpinterfacetestingapp.Model.models.responses.R2CommitResponse
import com.example.demokmpinterfacetestingapp.Model.models.responses.R2FilePresignResponse

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess


class CloudFilesRepositoryAndroid(val clientWithBearer: HttpClient, val cleanClient: HttpClient) : CloudFilesRepository {


    val fileBaseUrl = CustomApiParams.getBaseUrl() + "/files"

    override suspend fun presignAppFileUpload(
        folder: String,
        file_basename: String?,
        mime: String,
        ext: String,
        visibility: Visibility
    ): R2FilePresignResponse {
        val request = R2FilePresignRequest(OwnerType.APP, null, folder, file_basename, mime, ext, Visibility.public)
        val response: HttpResponse = clientWithBearer.post("$fileBaseUrl/presign") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        if (!response.status.isSuccess()) {
            throw Exception("Failed to presign file upload: ${response.status} - ${response.bodyAsText()}")
        }

        return response.body()
    }

    override suspend fun presignUserFileUpload(
        owner_id: String,
        folder: String,
        file_basename: String?,
        mime: String,
        ext: String,
        visibility: Visibility
    ): R2FilePresignResponse {
        val request =
            R2FilePresignRequest(OwnerType.USER, owner_id, folder, file_basename, mime, ext, Visibility.private)
        val response: HttpResponse = clientWithBearer.post("$fileBaseUrl/presign") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        if (!response.status.isSuccess()) {
            throw Exception("Failed to presign file upload: ${response.status} - ${response.bodyAsText()}")
        }

        return response.body()
    }


    override suspend fun fetchFile(URI: String): ImageBitmap {
        val response: HttpResponse = cleanClient.get(URI)
        if (!response.status.isSuccess()) {
            throw Exception("Failed to fetch file: ${response.status} - ${response.bodyAsText()}")
        }
        val bytes: ByteArray = response.body()
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            ?: throw Exception("Failed to decode image bytes")
        return bitmap.asImageBitmap()
    }


    override suspend fun commitAppFile(key: String, tags: List<String>, checksum: String?) {

        val request = R2FileCommitRequest(OwnerType.APP, null, key, Visibility.public, tags)

        val response: HttpResponse = clientWithBearer.post("$fileBaseUrl/commit") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        if (!response.status.isSuccess()) {
            throw Exception("Failed to commit file upload: ${response.status}")
        }


    }


    override suspend fun commitUserFile(owner_id: String, key: String, tags: List<String>, checksum: String?) : R2CommitResponse {
        val request = R2FileCommitRequest(OwnerType.USER, owner_id, key, Visibility.private, tags)

        val response: HttpResponse = clientWithBearer.post("$fileBaseUrl/commit") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        if (!response.status.isSuccess()) {
            throw Exception("Failed to presign file upload: ${response.status}")
        }

        return response.body()

    }


    //WARNING: !!!!! AWS MAKES NECESSARY TO HAVE A CLIENT WITHOUT ANY BEARER SO CREATING A SPECIAL ONE FOR THAT PURPOSE !!!!!!
   override suspend fun uploadToR2(
        presignedUrl: String,
        fileBytes: ByteArray,
        contentType: String
    ): Boolean {
        return try {
            val response = cleanClient.put(presignedUrl) {
                setBody(fileBytes)
                header(HttpHeaders.ContentType, contentType)
                header(HttpHeaders.ContentLength, fileBytes.size.toString())
                //header("x-amz-content-sha256", "UNSIGNED-PAYLOAD")
            }
            response.status.isSuccess()
        } catch (e: Exception) {
            println("R2 upload error: ${e.message}")
            false
        }
    }

    override suspend fun getFilesListFromCloudDB(folder: String): List<String> {
       clientWithBearer.post("$fileBaseUrl/files") {}

        return emptyList()
    }

}