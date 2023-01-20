package com.baleshapp.pocketpaper.utils

import android.annotation.SuppressLint
import android.content.Context
import com.baleshapp.pocketpaper.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * Class for work with date and time properties
 */
class DateTimeUtil {

    /**
     * Method returns formatted date String property from Long
     */
    @SuppressLint("SimpleDateFormat")
    fun getDateString(date: Long): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
        return dateFormat.format(date)
    }

    /**
     * Method returns formatted time String property from Long
     */
    @SuppressLint("SimpleDateFormat")
    fun getTimeString(time: Long): String {
        val timeFormat = SimpleDateFormat("HH:mm")
        return timeFormat.format(time)
    }

    /**
     * Method returns time property in Long from hourOfDay and minute properties
     */
    fun getTimeLong(hourOfDay: Int, minute: Int): Long {
        val cal: Calendar = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
        cal.set(Calendar.MINUTE, minute)
        return cal.timeInMillis
    }

    /**
     * Method returns date property in Long from year,month and dayOfMonth properties
     */
    fun getDateLong(year: Int, month: Int, dayOfMonth: Int): Long {
        val cal: Calendar = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        return cal.time.time
    }

    /**
     * Method return nearest to today's date day's name
     * @return Today or Tomorrow or String date
     */
    @SuppressLint("SimpleDateFormat")
    fun getDayName(date: Long, context: Context): String {
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
                context.resources.getString(R.string.today)
            }
            sdf.format(date).equals(sdf.format(tomorrow)) -> {
                context.resources.getString(R.string.tomorrow)
            }
            else -> {
                getDateString(date)
            }
        }
    }

    /**
     * Method returns today date property in Long
     */
    fun getTodayDate(): Long {
        val cal: Calendar = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.time.time
    }

    /**
     * Method calculate time borders of today
     *
     * @return LongArray, first value - lower border, second value - upper border
     */
    fun getTodayDayBorders(): LongArray {
        val firstDate: Calendar = Calendar.getInstance()
        val secondDate: Calendar = Calendar.getInstance()
        firstDate.add(Calendar.DAY_OF_MONTH, -1)
        firstDate.set(Calendar.HOUR_OF_DAY, 23)
        firstDate.set(Calendar.MINUTE, 59)
        secondDate.set(Calendar.HOUR_OF_DAY, 23)
        secondDate.set(Calendar.MINUTE, 59)
        return longArrayOf(firstDate.time.time, secondDate.time.time)
    }

    /**
     * Method calculate time borders of day
     *
     * @return LongArray, first value - lower border, second value - upper border
     */
    fun getDayBorders(year: Int, month: Int, dayOfMonth: Int): LongArray {

        val calendar1 = Calendar.getInstance()
        calendar1.set(Calendar.HOUR_OF_DAY, 23)
        calendar1.set(Calendar.MINUTE, 59)
        calendar1.set(Calendar.SECOND, 0)
        calendar1.set(Calendar.MILLISECOND, 0)
        calendar1.set(Calendar.YEAR, year)
        calendar1.set(Calendar.MONTH, month)
        calendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth - 1)

        val calendar2 = Calendar.getInstance()
        calendar2.set(Calendar.HOUR_OF_DAY, 23)
        calendar2.set(Calendar.MINUTE, 59)
        calendar2.set(Calendar.SECOND, 0)
        calendar2.set(Calendar.MILLISECOND, 0)
        calendar2.set(Calendar.YEAR, year)
        calendar2.set(Calendar.MONTH, month)
        calendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        return longArrayOf(calendar1.time.time, calendar2.time.time)
    }
}