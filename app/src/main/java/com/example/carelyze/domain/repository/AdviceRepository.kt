package com.example.carelyze.domain.repository

import com.example.carelyze.domain.model.Advice

interface AdviceRepository {
    suspend fun getAdvices(category: String? = null): List<Advice>
    suspend fun getAdviceById(id: String): Advice?
    suspend fun toggleFavorite(adviceId: String): Boolean
}
