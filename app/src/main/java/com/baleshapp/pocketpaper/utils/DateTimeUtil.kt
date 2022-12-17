package com.baleshapp.pocketpaper.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtil {

    @SuppressLint("SimpleDateFormat")
    fun getDateString(date: Long): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
        return dateFormat.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun getTimeString(time: Long): String {
        val timeFormat = SimpleDateFormat("HH:mm")
        return timeFormat.format(time)
    }

    @SuppressLint("SimpleDateFormat")
    fun getDayNameOfPosition(position: Int): String {
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, position)
        val date: Date = calendar.time
        val simpleDateFormat = SimpleDateFormat("EEEE")
        return simpleDateFormat.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun getStringDateOfPosition(position: Int): String {
        val firstDate: Calendar = Calendar.getInstance()
        firstDate.add(Calendar.DAY_OF_MONTH, position)
        val date: Date = firstDate.time
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
        return simpleDateFormat.format(date)
    }

    fun getTimeLong(hourOfDay: Int, minute: Int): Long {
        val cal: Calendar = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
        cal.set(Calendar.MINUTE, minute)
        return cal.timeInMillis
    }

    fun getDateLong(year: Int, month: Int, dayOfMonth: Int): Long {
        val cal: Calendar = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.MILLISECOND, 0)
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        return cal.time.time
    }

    @SuppressLint("SimpleDateFormat")
    fun getDayName(date: Long): String {
        val cal: Calendar = Calendar.getInstance()
        val current = Date(date)
        val today = Date()
        var tomorrow: Date = today
        cal.time = tomorrow
        cal.add(Calendar.DAY_OF_MONTH, 1)
        tomorrow = cal.time
        val sdf = SimpleDateFormat("ddMMy")
        return when {
            sdf.format(current).equals(sdf.format(today)) -> {
                "Сегодня"
            }
            sdf.format(date).equals(sdf.format(tomorrow)) -> {
                "Завтра"
            }
            else -> {
                getDateString(date)
            }
        }
    }


    fun getDayBorders(dayNumber: Int): LongArray {
        val firstDate: Calendar = Calendar.getInstance()
        val secondDate: Calendar = Calendar.getInstance()
        firstDate.add(Calendar.DAY_OF_MONTH, dayNumber)
        secondDate.add(Calendar.DAY_OF_MONTH, dayNumber)
        firstDate.add(Calendar.DAY_OF_MONTH, -1)
        firstDate.set(Calendar.HOUR_OF_DAY, 23)
        firstDate.set(Calendar.MINUTE, 59)
        secondDate.set(Calendar.HOUR_OF_DAY, 23)
        secondDate.set(Calendar.MINUTE, 59)
        return longArrayOf(firstDate.time.time, secondDate.time.time)
    }

}