package com.baleshapp.pocketpaper.viewmodel.purchase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.baleshapp.pocketpaper.data.repository.PurchaseRepository

class PurchaseViewModelFactory(private val repository: PurchaseRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PurchaseViewModel(repository) as T
    }
}