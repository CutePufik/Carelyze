package com.example.carelyze.data.util

import java.net.URI

/**
 * Собирает URL картинки совета: сервер может отдать абсолютный URL или путь относительно API.
 */
object AdviceImageUrlResolver {

    fun resolve(apiBaseUrl: String, imageUrl: String?): String? {
        val raw = imageUrl?.trim().orEmpty()
        if (raw.isEmpty()) return null
        val base = apiBaseUrl.trimEnd('/')

        if (raw.startsWith("http://", ignoreCase = true) || raw.startsWith("https://", ignoreCase = true)) {
            return normalizeAbsoluteUrl(raw, base)
        }

        return if (raw.startsWith("/")) "$base$raw" else "$base/$raw"
    }

    private fun normalizeAbsoluteUrl(rawUrl: String, apiBaseUrl: String): String {
        val imageUri = runCatching { URI(rawUrl) }.getOrNull() ?: return rawUrl
        val imageHost = imageUri.host?.lowercase().orEmpty()
        if (imageHost != "localhost" && imageHost != "127.0.0.1" && imageHost != "0.0.0.0") {
            return rawUrl
        }

        val baseUri = runCatching { URI(apiBaseUrl) }.getOrNull() ?: return rawUrl
        if (baseUri.host.isNullOrBlank()) return rawUrl

        return runCatching {
            URI(
                baseUri.scheme ?: imageUri.scheme,
                imageUri.userInfo,
                baseUri.host,
                if (baseUri.port != -1) baseUri.port else imageUri.port,
                imageUri.path,
                imageUri.query,
                imageUri.fragment
            ).toString()
        }.getOrElse { rawUrl }
    }
}
