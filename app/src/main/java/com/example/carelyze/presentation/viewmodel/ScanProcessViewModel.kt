package com.example.carelyze.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carelyze.domain.model.ScanResult
import com.example.carelyze.domain.model.ScannerType
import com.example.carelyze.domain.repository.ScannerRepository
import com.example.carelyze.domain.usecase.PredictScanUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ScanProcessViewModel @Inject constructor(
    private val scannerRepository: ScannerRepository,
    private val predictScanUseCase: PredictScanUseCase
) : ViewModel() {
    
    private val _selectedScanner = MutableStateFlow<ScannerType?>(null)
    val selectedScanner: StateFlow<ScannerType?> = _selectedScanner.asStateFlow()
    
    private val _scanResult = MutableStateFlow<ScanResult?>(null)
    val scanResult: StateFlow<ScanResult?> = _scanResult.asStateFlow()
    
    private val _isAnalyzing = MutableStateFlow<Boolean>(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun setScanner(scanner: ScannerType) {
        _selectedScanner.value = scanner
        _scanResult.value = null
    }
    
    fun loadScannerById(scannerId: String) {
        viewModelScope.launch {
            val scannerTypes = scannerRepository.getScannerTypes()
            val scannerType = scannerTypes.find { it.id == scannerId }
            scannerType?.let {
                setScanner(it)
            }
        }
    }
    
    fun analyzeImage(imageUri: String) {
        val scanner = _selectedScanner.value ?: return
        viewModelScope.launch {
            _isAnalyzing.value = true
            _error.value = null
            _scanResult.value = null
            try {
                _scanResult.value = predictScanUseCase(scannerId = scanner.id, imageUri = imageUri)
            } catch (e: Exception) {
                _error.value = e.message ?: "Не удалось выполнить анализ"
            } finally {
                _isAnalyzing.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
