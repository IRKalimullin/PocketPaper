package com.baleshapp.pocketpaper.viewmodel.task

import androidx.lifecycle.*
import com.baleshapp.pocketpaper.data.model.Task
import com.baleshapp.pocketpaper.data.repository.TaskRepository
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.util.*

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    fun insert(task: Task) = viewModelScope.launch {
        repository.insert(task)
    }

    fun getCurrentTasks(): LiveData<List<Task>> {
        return repository.getCurrentTasks().asLiveData()
    }

    fun delete(task: Task) = viewModelScope.launch {
        repository.delete(task)
    }

    fun update(task: Task) = viewModelScope.launch {
        repository.update(task)
    }

    fun getActiveTasks(): LiveData<List<Task>> {
        return repository.getActiveTasks().asLiveData()
    }

    fun getCompletedTasks(): LiveData<List<Task>> {
        return repository.getCompletedTasks().asLiveData()
    }

    fun getMissedTasks(): LiveData<List<Task>> {
        return repository.getMissedTasks().asLiveData()
    }

    fun getTasksDateList(): LiveData<List<Long>> {
        return repository.getDateList().asLiveData()
    }

    fun getTasksOnDate(date1: Long, date2: Long): LiveData<List<Task>> {
        return repository.getTasksOnDate(date1, date2).asLiveData()
    }

    fun reformatDate(longList: List<Long>): List<CalendarDay> {
        val list = mutableListOf<CalendarDay>()
        for (i in longList.indices) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = longList[i]
            val day = calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            list.add(CalendarDay.from(day.year, day.monthValue, day.dayOfMonth))
        }
        return list
    }
}



