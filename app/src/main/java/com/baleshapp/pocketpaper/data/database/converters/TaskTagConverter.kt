package com.baleshapp.pocketpaper.data.database.converters

import androidx.room.TypeConverter
import com.baleshapp.pocketpaper.data.model.TaskTag

class TaskTagConverter {

    @TypeConverter
    fun fromTaskTag(tag: TaskTag): String{
        return tag.name
    }

    @TypeConverter
    fun toTaskTag(tag: String): TaskTag{
        return when (tag){
            "GENERAL" -> TaskTag.GENERAL
            "PERSONAL" -> TaskTag.PERSONAL
            "WORK" -> TaskTag.WORK
            "STUDY" -> TaskTag.STUDY
            else -> TaskTag.GENERAL
        }
    }
}