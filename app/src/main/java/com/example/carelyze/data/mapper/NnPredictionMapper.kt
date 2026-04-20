package com.example.carelyze.data.mapper

import com.example.carelyze.data.dto.NnPredictionDto
import com.example.carelyze.domain.model.NnPrediction

object NnPredictionMapper {
    fun toDomain(dto: NnPredictionDto): NnPrediction {
        return NnPrediction(
            topClass = dto.topClass,
            topScore = dto.topScore,
            scores = dto.scores ?: dto.probs ?: emptyMap(),
            pPneumonia = dto.pPneumonia
        )
    }
}

