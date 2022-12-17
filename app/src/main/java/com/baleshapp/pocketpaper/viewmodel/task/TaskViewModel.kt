package com.baleshapp.pocketpaper.viewmodel.task

import androidx.lifecycle.*
import com.baleshapp.pocketpaper.data.model.Task
import com.baleshapp.pocketpaper.data.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    fun insert(task: Task) = viewModelScope.launch {
        repository.insert(task)
    }

    fun getCurrentTasks(dayOfWeekNumber: Int): LiveData<List<Task>> {
        return repository.getCurrentTasks(dayOfWeekNumber).asLiveData()
    }

    fun delete(task: Task) = viewModelScope.launch {
        repository.delete(task)
    }

    fun update(task: Task) = viewModelScope.launch {
        repository.update(task)
    }

}



