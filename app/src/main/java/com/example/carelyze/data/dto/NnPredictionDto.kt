package com.example.carelyze.data.dto

import com.google.gson.annotations.SerializedName

data class NnPredictionDto(
    @SerializedName("top_class")
    val topClass: String,
    @SerializedName("top_score")
    val topScore: Double,
    @SerializedName("scores")
    val scores: Map<String, Double>? = null,
    @SerializedName("probs")
    val probs: Map<String, Double>? = null,
    @SerializedName("p_pneumonia")
    val pPneumonia: Double? = null
)

