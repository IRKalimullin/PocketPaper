package com.baleshapp.pocketpaper.data.database

import androidx.room.*
import com.baleshapp.pocketpaper.data.model.Task
import com.baleshapp.pocketpaper.data.model.TaskTag
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM Task")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT date FROM Task GROUP BY date")
    fun getTasksDates(): Flow<List<Long>>

    @Query("SELECT * FROM Task WHERE date >:date1 AND date <:date2")
    fun getTasksOnDate(date1: Long,date2: Long): Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE date > :dateBefore AND isDone = 0")
    fun getActiveTasksLiveData(dateBefore: Long): Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE date < :dateBefore AND isDone = 0")
    fun getMissedTasksLiveData(dateBefore: Long): Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE isDone = 1")
    fun getCompletedTasksLiveData(): Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE (date BETWEEN :dateBefore AND :dateAfter) AND isDone = 0")
    fun getCurrentActiveTasks(dateBefore: Long, dateAfter: Long): Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE (date BETWEEN :dateBefore AND :dateAfter) AND isDone = 1")
    fun getCurrentCompletedTasks(dateBefore: Long, dateAfter: Long): Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE (date BETWEEN :dateBefore AND :dateAfter) AND isDone = 0 AND tag = :taskTag")
    fun getCurrentActiveFilteredTasks(dateBefore: Long, dateAfter: Long, taskTag: TaskTag): Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE (date BETWEEN :dateBefore AND :dateAfter) AND isDone = 1 AND tag = :taskTag")
    fun getCurrentCompletedFilteredTasks(dateBefore: Long, dateAfter: Long, taskTag: TaskTag): Flow<List<Task>>


    @Query("SELECT count(*) FROM Task where date BETWEEN :dateBefore AND :dateAfter")
    suspend fun getCountCurrentTasks(dateBefore: Long, dateAfter: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)
}