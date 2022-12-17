package com.baleshapp.pocketpaper.viewmodel.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.baleshapp.pocketpaper.data.repository.TaskRepository

class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskViewModel(repository) as T
    }
}