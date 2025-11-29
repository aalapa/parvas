package com.aravind.parva.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aravind.parva.data.model.HoldPeriod
import com.aravind.parva.data.model.MahaParva
import com.aravind.parva.data.repository.MahaParvaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for MahaParva details and all sub-levels
 * Manages a single Maha-Parva and its hierarchy
 */
class MahaParvaViewModel(
    private val repository: MahaParvaRepository,
    private val mahaParvaId: String
) : ViewModel() {

    private val _mahaParva = MutableStateFlow<MahaParva?>(null)
    val mahaParva: StateFlow<MahaParva?> = _mahaParva.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadMahaParva()
    }

    private fun loadMahaParva() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getMahaParvaById(mahaParvaId).collect { mahaParva ->
                _mahaParva.value = mahaParva
                _isLoading.value = false
            }
        }
    }

    fun updateParvaGoal(parvaIndex: Int, goal: String) {
        viewModelScope.launch {
            repository.updateParvaGoal(mahaParvaId, parvaIndex, goal)
        }
    }

    fun updateSaptahaGoal(parvaIndex: Int, saptahaIndex: Int, goal: String) {
        viewModelScope.launch {
            repository.updateSaptahaGoal(mahaParvaId, parvaIndex, saptahaIndex, goal)
        }
    }

    fun updateDina(
        dayNumber: Int,
        dailyIntention: String,
        notes: String,
        isCompleted: Boolean
    ) {
        viewModelScope.launch {
            repository.updateDina(mahaParvaId, dayNumber, dailyIntention, notes, isCompleted)
        }
    }

    fun updateHoldPeriods(holdPeriods: List<HoldPeriod>) {
        viewModelScope.launch {
            repository.updateHoldPeriods(mahaParvaId, holdPeriods)
        }
    }
}

