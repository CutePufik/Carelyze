package com.example.carelyze.domain.model

data class AuthResult(
    val accessToken: String,
    val userId: Int,
    val email: String,
)

data class UserProfile(
    val id: Int,
    val email: String,
    val fullName: String? = null,
)
