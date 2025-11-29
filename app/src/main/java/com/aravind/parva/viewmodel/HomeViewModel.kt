package com.aravind.parva.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aravind.parva.data.model.MahaParva
import com.aravind.parva.data.repository.MahaParvaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Home Screen
 * Manages the list of all Maha-Parvas
 */
class HomeViewModel(
    private val repository: MahaParvaRepository
) : ViewModel() {

    private val _mahaParvas = MutableStateFlow<List<MahaParva>>(emptyList())
    val mahaParvas: StateFlow<List<MahaParva>> = _mahaParvas.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadMahaParvas()
    }

    private fun loadMahaParvas() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.allMahaParvas.collect { mahaParvaList ->
                _mahaParvas.value = mahaParvaList
                _isLoading.value = false
            }
        }
    }

    fun createMahaParva(mahaParva: MahaParva) {
        viewModelScope.launch {
            repository.saveMahaParva(mahaParva)
        }
    }

    fun updateMahaParva(mahaParva: MahaParva) {
        viewModelScope.launch {
            repository.updateMahaParva(mahaParva)
        }
    }

    fun deleteMahaParva(mahaParva: MahaParva) {
        viewModelScope.launch {
            repository.deleteMahaParva(mahaParva)
        }
    }
}

