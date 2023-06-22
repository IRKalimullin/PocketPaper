package com.baleshapp.pocketpaper.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.baleshapp.pocketpaper.data.database.converters.StateConverter
import com.baleshapp.pocketpaper.data.database.converters.TaskPriorityConverter

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "isDone") var isDone: Boolean,
    @ColumnInfo(name = "date") var date: Long,
    @ColumnInfo(name = "time") var time: Long,
    @ColumnInfo(name = "timestampOfTask") var timestampOfTask: Long,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "folderId") var folderId: Int = 0,

    @TypeConverters(TaskPriorityConverter::class)
    @ColumnInfo(name = "priority") var priority: TaskPriority = TaskPriority.NONE,

    @TypeConverters(StateConverter::class)
    @ColumnInfo(name = "state") var state: States = States.CREATED
) : java.io.Serializable
