package com.example.carelyze.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carelyze.domain.model.Advice
import com.example.carelyze.domain.repository.AdviceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AdviceViewModel @Inject constructor(
    private val adviceRepository: AdviceRepository
) : ViewModel() {
    
    private val _advices = MutableStateFlow<List<Advice>>(emptyList())
    val advices: StateFlow<List<Advice>> = _advices.asStateFlow()
    
    private val _selectedAdvice = MutableStateFlow<Advice?>(null)
    val selectedAdvice: StateFlow<Advice?> = _selectedAdvice.asStateFlow()
    
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()
    
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        loadAdvices()
    }
    
    fun loadAdvices(category: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _selectedCategory.value = category
            try {
                val result = adviceRepository.getAdvices(category)
                _advices.value = result
                if (result.isEmpty()) {
                    _error.value = "Не удалось загрузить советы"
                }
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки: ${e.message}"
                _advices.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun selectAdvice(adviceId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _selectedAdvice.value = adviceRepository.getAdviceById(adviceId)
                if (_selectedAdvice.value == null) {
                    _error.value = "Совет не найден"
                }
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки: ${e.message}"
                _selectedAdvice.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun toggleFavorite(adviceId: String) {
        viewModelScope.launch {
            try {
                adviceRepository.toggleFavorite(adviceId)
                loadAdvices(_selectedCategory.value)
            } catch (e: Exception) {
                _error.value = "Ошибка при обновлении избранного: ${e.message}"
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}
