package com.example.carelyze.domain.repository

import com.example.carelyze.domain.model.ScanResult
import com.example.carelyze.domain.model.ScannerType

interface ScannerRepository {
    suspend fun getScannerTypes(): List<ScannerType>
    suspend fun analyzeImage(scannerType: ScannerType, imageUri: String): ScanResult
}
