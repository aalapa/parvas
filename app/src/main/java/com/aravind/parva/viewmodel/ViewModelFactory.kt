package com.aravind.parva.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aravind.parva.data.repository.MahaParvaRepository

/**
 * Factory for creating ViewModels with dependencies
 */
class HomeViewModelFactory(
    private val repository: MahaParvaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

/**
 * Factory for creating MahaParvaViewModel with specific ID
 */
class MahaParvaViewModelFactory(
    private val repository: MahaParvaRepository,
    private val mahaParvaId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MahaParvaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MahaParvaViewModel(repository, mahaParvaId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

