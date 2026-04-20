package com.example.carelyze.presentation.viewmodel

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class OnboardingViewModel @Inject constructor() : ViewModel() {
    val slides = listOf(
        OnboardingSlide(
            title = "Ваш AI‑помощник",
            description = "Ваш AI‑помощник для здоровья — анализируйте снимки и получайте доверенные рекомендации от сертифицированных алгоритмов."
        ),
        OnboardingSlide(
            title = "Мгновенный анализ снимков",
            description = "Поддержка кожи, лёгких, глаз, ногтей и общего фотоанализа — 7 нейросетей готовы к работе."
        ),
        OnboardingSlide(
            title = "Советы и тесты",
            description = "Доступ к свежим статьям от медиа-партнёров и интерактивным тестам по стрессу, сердцу и диабету."
        )
    )
}

data class OnboardingSlide(
    val title: String,
    val description: String
)
