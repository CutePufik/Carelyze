package com.example.carelyze.data.repository

import com.example.carelyze.data.api.ApiService
import com.example.carelyze.data.dto.UserLoginDto
import com.example.carelyze.data.dto.UserRegisterDto
import com.example.carelyze.data.local.PreferencesManager
import com.example.carelyze.domain.model.AuthResult
import com.example.carelyze.domain.model.UserProfile
import com.example.carelyze.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val preferencesManager: PreferencesManager,
) : AuthRepository {

    override suspend fun register(email: String, password: String, fullName: String?): Result<AuthResult> {
        return try {
            val response = apiService.register(
                UserRegisterDto(
                    email = email.trim().lowercase(),
                    password = password,
                    fullName = fullName?.trim()?.takeIf { it.isNotBlank() },
                )
            )

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val user = body.user ?: return Result.failure(
                    IllegalStateException("Ответ сервера не содержит данных пользователя")
                )
                preferencesManager.setAccessToken(body.accessToken)
                Result.success(
                    AuthResult(
                        accessToken = body.accessToken,
                        userId = user.id,
                        email = user.email,
                    )
                )
            } else {
                Result.failure(IllegalStateException("Не удалось зарегистрироваться"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, password: String): Result<AuthResult> {
        return try {
            val response = apiService.login(
                UserLoginDto(
                    email = email.trim().lowercase(),
                    password = password,
                )
            )

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val user = body.user ?: return Result.failure(
                    IllegalStateException("Ответ сервера не содержит данных пользователя")
                )
                preferencesManager.setAccessToken(body.accessToken)
                Result.success(
                    AuthResult(
                        accessToken = body.accessToken,
                        userId = user.id,
                        email = user.email,
                    )
                )
            } else {
                Result.failure(IllegalStateException("Неверный email или пароль"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): Result<UserProfile> {
        val token = preferencesManager.getAccessToken()
            ?: return Result.failure(IllegalStateException("Токен не найден"))

        return try {
            val response = apiService.getCurrentUser("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                Result.success(
                    UserProfile(
                        id = body.id,
                        email = body.email,
                        fullName = body.fullName,
                    )
                )
            } else {
                Result.failure(IllegalStateException("Не удалось получить пользователя"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun hasToken(): Boolean {
        return !preferencesManager.getAccessToken().isNullOrBlank()
    }

    override fun logout() {
        preferencesManager.clearAccessToken()
    }
}
