package com.baleshapp.pocketpaper.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "isDone") var isDone: Boolean,
    @ColumnInfo(name = "date") var date: Long,
    @ColumnInfo(name = "time") var time: Long,
    @ColumnInfo(name = "timestampOfTask") var timestampOfTask: Long
)
