package com.baleshapp.pocketpaper.data.repository

import android.content.Context
import com.baleshapp.pocketpaper.data.database.AppDatabase
import com.baleshapp.pocketpaper.data.database.TaskDao
import com.baleshapp.pocketpaper.data.model.Task
import com.baleshapp.pocketpaper.utils.DateTimeUtil
import kotlinx.coroutines.flow.Flow

class TaskRepository(context: Context) {

    private val taskDao: TaskDao

    init {
        val database = AppDatabase.getDatabase(context)
        taskDao = database!!.taskDao()
    }

    fun getCurrentTasks(dayOfWeekNumber: Int): Flow<List<Task>> {
        val day = DateTimeUtil().getDayBorders(dayOfWeekNumber)
        return taskDao.getCurrentTasks(day[0], day[1])
    }

    fun getActiveTasks(): Flow<List<Task>> {
        val day = DateTimeUtil().getDayBorders(0)
        return taskDao.getActiveTasksLiveData(day.first())
    }

    fun getCompletedTasks(): Flow<List<Task>> {
        return taskDao.getCompletedTasksLiveData()
    }

    fun getMissedTasks(): Flow<List<Task>> {
        val day = DateTimeUtil().getDayBorders(0)
        return taskDao.getMissedTasksLiveData(day.first())
    }

    fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks()
    }

    suspend fun insert(task: Task) {
        taskDao.insert(task)
    }

    suspend fun update(task: Task) {
        taskDao.update(task)
    }

    suspend fun delete(task: Task) {
        taskDao.delete(task)
    }
}