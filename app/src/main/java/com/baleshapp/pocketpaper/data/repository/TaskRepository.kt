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

    fun getCurrentActiveTasks(): Flow<List<Task>> {
        val day = DateTimeUtil().getTodayDayBorders()
        return taskDao.getCurrentActiveTasks(day[0], day[1])
    }

    fun getCurrentCompletedTasks(): Flow<List<Task>> {
        val day = DateTimeUtil().getTodayDayBorders()
        return taskDao.getCurrentCompletedTasks(day[0], day[1])
    }

    fun getDateList(): Flow<List<Long>>{
        return taskDao.getTasksDates()
    }

    fun getTasksOnDate(date1: Long,date2: Long): Flow<List<Task>>{
        return taskDao.getTasksOnDate(date1,date2)
    }

    fun getActiveTasks(): Flow<List<Task>> {
        val day = DateTimeUtil().getTodayDayBorders()
        return taskDao.getActiveTasksLiveData(day.first())
    }

    fun getCompletedTasks(): Flow<List<Task>> {
        return taskDao.getCompletedTasksLiveData()
    }

    fun getMissedTasks(): Flow<List<Task>> {
        val day = DateTimeUtil().getTodayDayBorders()
        return taskDao.getMissedTasksLiveData(day.first())
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