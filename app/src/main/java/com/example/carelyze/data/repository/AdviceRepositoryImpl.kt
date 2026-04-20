package com.example.carelyze.data.repository

import android.util.Log
import com.example.carelyze.data.api.ApiService
import com.example.carelyze.data.mapper.AdviceMapper
import com.example.carelyze.data.util.AdviceImageUrlResolver
import com.example.carelyze.domain.model.Advice
import com.example.carelyze.domain.repository.AdviceRepository
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

class AdviceRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    @Named("api_base_url") private val apiBaseUrl: String
) : AdviceRepository {
    
    private val favorites = mutableSetOf<String>() // Локальное хранилище избранного
    private val TAG = "AdviceRepository"
    
    override suspend fun getAdvices(category: String?): List<Advice> {
        return try {
            Log.d(TAG, "Запрос всех советов, категория: $category")
            val response = apiService.getAllAdvices()
            Log.d(TAG, "Ответ получен: isSuccessful=${response.isSuccessful}, code=${response.code()}")
            
            if (response.isSuccessful && response.body() != null) {
                val dtoList = response.body()!!
                Log.d(TAG, "Получено советов: ${dtoList.size}")
                val advices = AdviceMapper.toDomainList(dtoList).map { advice ->
                    advice.copy(imageUrl = AdviceImageUrlResolver.resolve(apiBaseUrl, advice.imageUrl))
                }
                // Применяем фильтр по категории на стороне клиента
                val filtered = if (category == null) {
                    advices
                } else {
                    advices.filter { it.category == category }
                }
                Log.d(TAG, "После фильтрации: ${filtered.size}")
                // Восстанавливаем состояние избранного
                filtered.map { advice ->
                    advice.copy(isFavorite = favorites.contains(advice.id))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "Ошибка ответа: code=${response.code()}, message=${response.message()}, body=$errorBody")
                emptyList()
            }
        } catch (e: IOException) {
            Log.e(TAG, "Сетевая ошибка: ${e.message}", e)
            emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Неожиданная ошибка: ${e.message}", e)
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getAdviceById(id: String): Advice? {
        return try {
            val adviceId = id.toIntOrNull() ?: run {
                Log.e(TAG, "Неверный ID совета: $id")
                return null
            }
            Log.d(TAG, "Запрос совета по ID: $adviceId")
            val response = apiService.getAdviceById(adviceId)
            Log.d(TAG, "Ответ получен: isSuccessful=${response.isSuccessful}, code=${response.code()}")
            
            if (response.isSuccessful && response.body() != null) {
                val advice = AdviceMapper.toDomain(response.body()!!)
                advice.copy(
                        imageUrl = AdviceImageUrlResolver.resolve(apiBaseUrl, advice.imageUrl),
                        isFavorite = favorites.contains(advice.id)
                )
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "Ошибка ответа: code=${response.code()}, message=${response.message()}, body=$errorBody")
                null
            }
        } catch (e: IOException) {
            Log.e(TAG, "Сетевая ошибка: ${e.message}", e)
            null
        } catch (e: Exception) {
            Log.e(TAG, "Неожиданная ошибка: ${e.message}", e)
            e.printStackTrace()
            null
        }
    }

    override suspend fun toggleFavorite(adviceId: String): Boolean {
        return if (favorites.contains(adviceId)) {
            favorites.remove(adviceId)
            false
        } else {
            favorites.add(adviceId)
            true
        }
    }
}
