package com.example.carelyze.domain.repository

import com.example.carelyze.domain.model.AuthResult
import com.example.carelyze.domain.model.UserProfile

interface AuthRepository {
    suspend fun register(email: String, password: String, fullName: String?): Result<AuthResult>
    suspend fun login(email: String, password: String): Result<AuthResult>
    suspend fun getCurrentUser(): Result<UserProfile>
    fun hasToken(): Boolean
    fun logout()
}
