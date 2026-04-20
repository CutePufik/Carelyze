package com.example.carelyze.data.dto

import com.google.gson.annotations.SerializedName

data class AdviceDto(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("category")
    val category: String,
    
    @SerializedName("image_url")
    val imageUrl: String?,
    
    @SerializedName("preview_text")
    val previewText: String?,
    
    @SerializedName("full_text")
    val fullText: String?,

    @SerializedName("reading_time")
    val readingTime: Int?,

    @SerializedName("created_at")
    val createdAt: String?
)
