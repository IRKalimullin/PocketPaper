package com.baleshapp.pocketpaper.data.repository

import android.content.Context
import com.baleshapp.pocketpaper.data.database.AppDatabase
import com.baleshapp.pocketpaper.data.database.HabitDao
import com.baleshapp.pocketpaper.data.model.Habit
import com.baleshapp.pocketpaper.data.model.HabitPoint
import kotlinx.coroutines.flow.Flow

class HabitRepository(context: Context) {

    private val habitDao: HabitDao

    init {
        val database = AppDatabase.getDatabase(context)
        habitDao = database!!.habitDao()
    }

    fun getHabitList(): Flow<List<Habit>> {
        return habitDao.getHabitList()
    }

    suspend fun updateHabitPoint(habitPoint: HabitPoint) {
        habitDao.updateHabitPoint(habitPoint)
    }

    fun getHabitPointOnDate(date1: Long, habitId: Int): Flow<HabitPoint?> {
        return habitDao.getHabitPointOnDate(date1, habitId)
    }

    fun getHabitPointList(habitId: Int): Flow<List<HabitPoint>> {
        return habitDao.getHabitPointList(habitId)
    }

    suspend fun insertHabit(habit: Habit) {
        habitDao.insertHabit(habit)
    }

    suspend fun insertHabitPoint(habitPoint: HabitPoint) {
        habitDao.insertHabitPoint(habitPoint)
    }

    suspend fun update(habit: Habit) {
        habitDao.updateHabit(habit)
    }

    suspend fun deleteHabit(habit: Habit) {
        habitDao.deleteHabit(habit)
    }

    suspend fun deleteHabitPoint(habitId: Int) {
        habitDao.deleteHabitPoint(habitId)
    }
}