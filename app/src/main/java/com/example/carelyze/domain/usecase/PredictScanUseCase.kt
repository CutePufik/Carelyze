package com.example.carelyze.domain.usecase

import com.example.carelyze.domain.model.NnPrediction
import com.example.carelyze.domain.model.ScanResult
import com.example.carelyze.domain.repository.NnRepository
import java.util.Locale
import javax.inject.Inject
import kotlin.math.roundToInt

class PredictScanUseCase @Inject constructor(
    private val nnRepository: NnRepository
) {
    suspend operator fun invoke(scannerId: String, imageUri: String): ScanResult {
        val prediction = nnRepository.predict(scannerId = scannerId, imageUri = imageUri)
        val pct = (prediction.topScore.coerceIn(0.0, 1.0) * 100.0).roundToInt().coerceIn(0, 100)
        val description = buildDescription(scannerId, prediction)
        return ScanResult(
            diagnosis = localizeMlLabel(scannerId = scannerId, rawLabel = prediction.topClass),
            probability = pct,
            description = description,
            recommendations = "Результат носит справочный характер. Для постановки диагноза обратитесь к врачу."
        )
    }

    private fun buildDescription(scannerId: String, prediction: NnPrediction): String {
        val localizedTopClass = localizeMlLabel(scannerId = scannerId, rawLabel = prediction.topClass)
        val base = String.format(
            Locale.US,
            "Результат нейросети. Класс «%s», уверенность: %.6f (~%d%%).",
            localizedTopClass,
            prediction.topScore,
            (prediction.topScore.coerceIn(0.0, 1.0) * 100.0).roundToInt().coerceIn(0, 100)
        )
        if (scannerId != "lung") return base

        val parts = mutableListOf(base)
        if (prediction.scores.isNotEmpty()) {
            val line = prediction.scores.entries.joinToString(", ") { (k, v) ->
                "${localizeMlLabel(scannerId = scannerId, rawLabel = k)}: ${String.format(Locale.US, "%.4f", v)}"
            }
            parts.add("Оценки классов: $line.")
        }
        prediction.pPneumonia?.let {
            parts.add("Вероятность пневмонии = ${String.format(Locale.US, "%.6f", it)}.")
        }
        return parts.joinToString(" ")
    }

    private fun localizeMlLabel(scannerId: String, rawLabel: String): String {
        val normalized = rawLabel.trim().uppercase(Locale.ROOT)
        return when (scannerId) {
            "lung" -> when (normalized) {
                "NORMAL" -> "Норма"
                "PNEUMONIA" -> "Пневмония"
                else -> humanizeLabel(rawLabel)
            }
            "skin" -> when (normalized) {
                "BENIGN", "NON_MALIGNANT", "NON-MALIGNANT" -> "Доброкачественное"
                "MALIGNANT" -> "Злокачественное"
                else -> humanizeLabel(rawLabel)
            }
            "brain" -> when (normalized) {
                "GLIOMA" -> "Глиома"
                "MENINGIOMA" -> "Менингиома"
                "PITUITARY" -> "Опухоль гипофиза"
                "NO_TUMOR", "NO-TUMOR", "NOTUMOR" -> "Опухоль не обнаружена"
                else -> humanizeLabel(rawLabel)
            }
            "alzheimer" -> when (normalized) {
                "NON_DEMENTED", "NON-DEMENTED" -> "Без деменции"
                "VERY_MILD_DEMENTED", "VERY-MILD-DEMENTED" -> "Очень легкая деменция"
                "MILD_DEMENTED", "MILD-DEMENTED" -> "Легкая деменция"
                "MODERATE_DEMENTED", "MODERATE-DEMENTED" -> "Умеренная деменция"
                "NONDEMENTED" -> "Без деменции"
                "VERYMILDDEMENTED" -> "Очень легкая деменция"
                "MILDDEMENTED" -> "Легкая деменция"
                "MODERATEDEMENTED" -> "Умеренная деменция"
                else -> humanizeLabel(rawLabel)
            }
            else -> humanizeLabel(rawLabel)
        }
    }

    private fun humanizeLabel(rawLabel: String): String {
        return rawLabel
            .replace('_', ' ')
            .replace('-', ' ')
            .trim()
    }
}

