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

    fun getActiveTasks() : LiveData<List<Task>>  {
        return repository.getActiveTasks().asLiveData()
    }

    fun getCompletedTasks() : LiveData<List<Task>> {
        return repository.getCompletedTasks().asLiveData()
    }

    fun getMissedTasks(): LiveData<List<Task>>  {
        return repository.getMissedTasks().asLiveData()
    }

    fun getTasksDateList(): LiveData<List<Long>>{
        return repository.getDateList().asLiveData()
    }

    fun getTasksOnDate(date1: Long,date2: Long): LiveData<List<Task>>{
        return repository.getTasksOnDate(date1,date2).asLiveData()
    }
}



