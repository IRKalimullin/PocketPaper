package com.baleshapp.pocketpaper.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.baleshapp.pocketpaper.data.model.Habit
import com.baleshapp.pocketpaper.data.model.HabitPoint
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Query("SELECT * FROM Habit")
    fun getHabitList(): Flow<List<Habit>>

    @Query("SELECT * FROM HabitPoint WHERE habitId = :habitId ORDER BY date DESC")
    fun getHabitPointList(habitId: Int): Flow<List<HabitPoint>>

    @Query("SELECT * FROM HabitPoint WHERE date =:date1 AND habitId = :habitId")
    fun getHabitPointOnDate(date1: Long, habitId: Int): Flow<HabitPoint?>

    @Insert
    suspend fun insertHabit(habit: Habit)

    @Insert
    suspend fun insertHabitPoint(point: HabitPoint)

    @Update
    suspend fun updateHabit(habit: Habit)

    @Update
    suspend fun updateHabitPoint(point: HabitPoint)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("DELETE FROM HabitPoint WHERE habitId = :habitId")
    suspend fun deleteHabitPoint(habitId: Int)
}