package com.example.carelyze.domain.model

data class ScanResult(
    val diagnosis: String,
    val probability: Int,
    val description: String,
    val recommendations: String
)
