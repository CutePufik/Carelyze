package com.example.carelyze.domain.repository

import com.example.carelyze.domain.model.NnPrediction

interface NnRepository {
    suspend fun predict(scannerId: String, imageUri: String): NnPrediction
}

