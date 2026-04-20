package com.example.carelyze.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carelyze.domain.model.ScannerType
import com.example.carelyze.domain.repository.ScannerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ScannerViewModel @Inject constructor(
    private val scannerRepository: ScannerRepository
) : ViewModel() {
    
    private val _scannerTypes = MutableStateFlow<List<ScannerType>>(emptyList())
    val scannerTypes: StateFlow<List<ScannerType>> = _scannerTypes.asStateFlow()
    
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadScannerTypes()
    }
    
    private fun loadScannerTypes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _scannerTypes.value = scannerRepository.getScannerTypes()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
