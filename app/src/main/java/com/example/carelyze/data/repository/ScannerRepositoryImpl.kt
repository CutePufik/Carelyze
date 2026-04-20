package com.example.carelyze.data.repository

import com.example.carelyze.domain.model.ScanResult
import com.example.carelyze.domain.model.ScannerType
import com.example.carelyze.domain.repository.ScannerRepository
import javax.inject.Inject

class ScannerRepositoryImpl @Inject constructor() : ScannerRepository {
    override suspend fun getScannerTypes(): List<ScannerType> {
        return listOf(
            ScannerType(
                id = "lung",
                title = "Рентген лёгких",
                hint = "Снимок грудной клетки",
                color = 0xFF63B0FF.toInt()
            ),
            ScannerType(
                id = "brain",
                title = "Опухоль мозга",
                hint = "МРТ или КТ головы",
                color = 0xFF9B6CFF.toInt()
            ),
            ScannerType(
                id = "alzheimer",
                title = "Альцгеймер (МРТ)",
                hint = "Снимок структуры мозга",
                color = 0xFF4DD5C3.toInt()
            )
        )
    }

    override suspend fun analyzeImage(scannerType: ScannerType, imageUri: String): ScanResult {
        throw UnsupportedOperationException(
            "analyzeImage не используется: анализ идёт через PredictScanUseCase → NnRepository (реальный API)."
        )
    }
}
