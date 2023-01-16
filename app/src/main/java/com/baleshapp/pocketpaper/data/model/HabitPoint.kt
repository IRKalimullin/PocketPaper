package com.baleshapp.pocketpaper.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HabitPoint(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "habitId") var habitId: Int,
    @ColumnInfo(name = "isDone") var isDone: Boolean,
    @ColumnInfo(name = "value") var value: Int,
    @ColumnInfo(name = "date") var date: Long
)