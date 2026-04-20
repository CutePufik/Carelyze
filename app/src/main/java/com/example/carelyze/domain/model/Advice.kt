package com.example.carelyze.domain.model

data class Advice(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val imageUrl: String? = null,
    val content: String? = null,
    val author: String? = null,
    val viewsCount: Int = 0,
    val rating: Double = 0.0,
    val tags: List<String> = emptyList(),
    val readingTime: Int? = null,
    val isFeatured: Boolean = false,
    val isFavorite: Boolean = false
)
