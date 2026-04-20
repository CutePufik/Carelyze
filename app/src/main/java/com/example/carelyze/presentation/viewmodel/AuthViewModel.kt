package com.example.carelyze.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carelyze.domain.model.UserProfile
import com.example.carelyze.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _isLoginMode = MutableStateFlow(true)
    val isLoginMode: StateFlow<Boolean> = _isLoginMode.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    private val _currentUser = MutableStateFlow<UserProfile?>(null)
    val currentUser: StateFlow<UserProfile?> = _currentUser.asStateFlow()

    fun setLoginMode(isLogin: Boolean) {
        _isLoginMode.value = isLogin
        _error.value = null
    }

    fun onEmailChange(value: String) {
        _email.value = value
    }

    fun onPasswordChange(value: String) {
        _password.value = value
    }

    fun onFullNameChange(value: String) {
        _fullName.value = value
    }

    fun submit() {
        if (_email.value.isBlank() || _password.value.isBlank()) {
            _error.value = "Заполните email и пароль"
            return
        }
        if (!_isLoginMode.value && _password.value.length < 6) {
            _error.value = "Пароль должен быть не короче 6 символов"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = if (_isLoginMode.value) {
                authRepository.login(
                    email = _email.value,
                    password = _password.value,
                )
            } else {
                authRepository.register(
                    email = _email.value,
                    password = _password.value,
                    fullName = _fullName.value,
                )
            }

            result.onSuccess {
                _isAuthenticated.value = true
            }.onFailure {
                _error.value = it.message ?: "Ошибка авторизации"
            }

            _isLoading.value = false
        }
    }

    fun loadCurrentUser() {
        if (!authRepository.hasToken()) {
            _isAuthenticated.value = false
            return
        }

        viewModelScope.launch {
            val result = authRepository.getCurrentUser()
            result.onSuccess {
                _currentUser.value = it
                _isAuthenticated.value = true
            }.onFailure {
                _isAuthenticated.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
