package com.example.carelyze.data.repository

import android.content.Context
import android.net.Uri
import com.example.carelyze.data.api.ApiService
import com.example.carelyze.data.local.PreferencesManager
import com.example.carelyze.data.mapper.NnPredictionMapper
import com.example.carelyze.domain.model.NnPrediction
import com.example.carelyze.domain.repository.NnRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import javax.inject.Inject
import androidx.core.net.toUri

class NnRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val context: Context
) : NnRepository {

    override suspend fun predict(scannerId: String, imageUri: String): NnPrediction {
        val accessToken = PreferencesManager.getInstance(context).getAccessToken()
            ?: throw IOException("No access token. Please login again.")
        val authorization = if (accessToken.startsWith("Bearer ", ignoreCase = true)) {
            accessToken
        } else {
            "Bearer $accessToken"
        }

        val uri = imageUri.toUri()
        val part = buildImagePart(uri)

        val response = when (scannerId) {
            // текущие scannerId из приложения
            "skin" -> apiService.predictSkinDisease(authorization, part)
            "lung" -> apiService.predictLungXray(authorization, part)

            // на будущее / если добавишь такие сканеры
            "brain" -> apiService.predictBrainTumor(authorization, part)
            "alzheimer" -> apiService.predictAlzheimer(authorization, part)

            else -> throw IllegalArgumentException("Unsupported scannerId for NN: $scannerId")
        }

        if (!response.isSuccessful || response.body() == null) {
            val errorBody = response.errorBody()?.string()
            throw IOException("NN request failed: code=${response.code()} message=${response.message()} body=$errorBody")
        }

        return NnPredictionMapper.toDomain(response.body()!!)
    }

    private fun buildImagePart(uri: Uri): MultipartBody.Part {
        val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            ?: throw IOException("Cannot open image uri: $uri")

        val (filename, mediaType) = resolveFilenameAndMime(uri)
        val requestBody = bytes.toRequestBody(mediaType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(
            name = "file",
            filename = filename,
            body = requestBody
        )
    }

    /**
     * OkHttp/Spring не принимают wildcard в subtype (например image со звёздочкой) в Content-Type части multipart.
     */
    private fun resolveFilenameAndMime(uri: Uri): Pair<String, String> {
        val resolverType = context.contentResolver.getType(uri)
        val safeMime = resolverType?.takeIf { it.isNotBlank() && !it.contains('*') }

        val path = uri.lastPathSegment.orEmpty()
        val ext = path.substringAfterLast('.', "").lowercase()
        val filename = when {
            path.isNotBlank() && ext.isNotBlank() -> path
            else -> "image.jpg"
        }

        val mime = safeMime ?: when (ext) {
            "png" -> "image/png"
            "webp" -> "image/webp"
            "gif" -> "image/gif"
            "jpg", "jpeg" -> "image/jpeg"
            else -> "image/jpeg"
        }
        return Pair(filename, mime)
    }
}

