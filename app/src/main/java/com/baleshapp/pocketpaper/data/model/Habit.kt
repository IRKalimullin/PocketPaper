package com.baleshapp.pocketpaper.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Habit(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "startDate") var startDate: Long,
    @ColumnInfo(name = "repetition") var repetition: Int,
    @ColumnInfo(name = "isCheckable") var isCheckable: Boolean
)