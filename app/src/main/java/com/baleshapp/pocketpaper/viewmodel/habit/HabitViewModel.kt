package com.baleshapp.pocketpaper.viewmodel.habit

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.baleshapp.pocketpaper.data.model.Habit
import com.baleshapp.pocketpaper.data.model.HabitPoint
import com.baleshapp.pocketpaper.data.repository.HabitRepository
import com.baleshapp.pocketpaper.utils.DateTimeUtil
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.util.*

class HabitViewModel(private val repository: HabitRepository) : ViewModel() {

    fun createStartDatePoint(habit: Habit){
        val habitPoint =
            HabitPoint(habitId = habit.id, isDone = false, value = 0, date = habit.startDate)
        insertHabitPoint(habitPoint)
    }

    fun updateHabit(habit: Habit) = viewModelScope.launch {
        repository.update(habit)
    }

    private fun createInitHabitPoint(habitId: Int, lastDate: Long) {
        val todayCalendar = Calendar.getInstance()
        val startDateCalendar = Calendar.getInstance()

        todayCalendar.timeInMillis = DateTimeUtil().getTodayDate()
        startDateCalendar.timeInMillis = lastDate
        val daysBetween =
            todayCalendar.get(Calendar.DAY_OF_MONTH) - startDateCalendar.get(Calendar.DAY_OF_MONTH)

        for (i in 0 until daysBetween){

            startDateCalendar.add(Calendar.DAY_OF_MONTH,1)
            val habitPoint =
                HabitPoint(habitId = habitId, isDone = false, value = 0, date = startDateCalendar.time.time)
            insertHabitPoint(habitPoint)
        }

    }

    private fun checkHabitPoint(habitPoint: HabitPoint) {
        val today = DateTimeUtil().getTodayDate()
        if (habitPoint.date < today) {
            createInitHabitPoint(habitPoint.habitId,habitPoint.date)
        }
    }

    private fun reformatDate(habitPoint: HabitPoint): CalendarDay {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = habitPoint.date
        val day = calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        return CalendarDay.from(day.year, day.monthValue, day.dayOfMonth)
    }

    fun getMissedHabitPoints(habitPointDates: List<HabitPoint>): List<CalendarDay> {
        checkHabitPoint(habitPointDates.first())
        val list = mutableListOf<CalendarDay>()
        for (i in habitPointDates.indices) {
            if (!habitPointDates[i].isDone) {
                list.add(reformatDate(habitPointDates[i]))
            }
        }
        return list
    }

    fun getCompletedHabitPoints(habitPointDates: List<HabitPoint>): List<CalendarDay> {
        val list = mutableListOf<CalendarDay>()
        for (i in habitPointDates.indices) {
            if (habitPointDates[i].isDone) {
                list.add(reformatDate(habitPointDates[i]))
            }
        }
        return list
    }

    fun fullDeleteHabit(habit: Habit){
        deleteHabit(habit)
        deleteHabitPoint(habit.id)
    }

    private fun deleteHabitPoint(habitId: Int) = viewModelScope.launch {
        repository.deleteHabitPoint(habitId)
    }

    fun getHabitList(): LiveData<List<Habit>> {
        return repository.getHabitList().asLiveData()
    }

    fun getHabitPointList(habitId: Int): LiveData<List<HabitPoint>> {
        return repository.getHabitPointList(habitId).asLiveData()
    }

    fun saveHabit(habit: Habit) {
        insertHabit(habit)
    }

    private fun insertHabit(habit: Habit) = viewModelScope.launch {
        repository.insertHabit(habit)
    }

    private fun deleteHabit(habit: Habit) = viewModelScope.launch {
        repository.deleteHabit(habit)
    }

    fun insertHabitPoint(habitPoint: HabitPoint) = viewModelScope.launch {
        repository.insertHabitPoint(habitPoint)
    }

    fun updateHabitPoint(habitPoint: HabitPoint) = viewModelScope.launch {
        repository.updateHabitPoint(habitPoint)
    }

    fun getHabitPointOnDate(date: Long, habitId: Int): LiveData<HabitPoint?> {
        return repository.getHabitPointOnDate(date, habitId).asLiveData()
    }

}