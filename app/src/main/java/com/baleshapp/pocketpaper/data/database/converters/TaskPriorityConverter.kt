package com.baleshapp.pocketpaper.data.database.converters

import androidx.room.TypeConverter
import com.baleshapp.pocketpaper.data.model.TaskPriority

class TaskPriorityConverter {

    @TypeConverter
    fun fromTaskPriority(priority: TaskPriority): String {
        return priority.name
    }

    @TypeConverter
    fun toTaskPriority(priorityName: String): TaskPriority {
        return when (priorityName) {
            "NONE" -> TaskPriority.NONE
            "LOW" -> TaskPriority.LOW
            "MEDIUM" -> TaskPriority.MEDIUM
            "HIGH" -> TaskPriority.HIGH
            else -> TaskPriority.NONE
        }
    }
}