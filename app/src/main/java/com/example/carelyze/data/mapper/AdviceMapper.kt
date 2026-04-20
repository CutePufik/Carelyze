package com.example.carelyze.data.mapper

import com.example.carelyze.data.dto.AdviceDto
import com.example.carelyze.domain.model.Advice

object AdviceMapper {
    
    fun toDomain(dto: AdviceDto): Advice {
        return Advice(
            id = dto.id.toString(),
            title = dto.title,
            description = dto.previewText ?: "",
            category = dto.category,
            imageUrl = dto.imageUrl,
            content = dto.fullText,
            readingTime = dto.readingTime,
            isFavorite = false // По умолчанию не в избранном
        )
    }
    
    fun toDomainList(dtoList: List<AdviceDto>): List<Advice> {
        return dtoList.map { toDomain(it) }
    }
}
