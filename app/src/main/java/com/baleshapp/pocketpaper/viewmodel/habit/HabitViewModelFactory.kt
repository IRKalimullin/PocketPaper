package com.baleshapp.pocketpaper.viewmodel.habit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.baleshapp.pocketpaper.data.repository.HabitRepository

class HabitViewModelFactory(private val repository: HabitRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HabitViewModel(repository) as T
    }
}
