package com.example.carelyze.domain.model

data class NnPrediction(
    val topClass: String,
    val topScore: Double,
    val scores: Map<String, Double>,
    val pPneumonia: Double? = null
)

