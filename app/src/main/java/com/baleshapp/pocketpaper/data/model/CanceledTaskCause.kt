package com.baleshapp.pocketpaper.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CanceledTaskCause(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "cause") var taskId: Int,
    @ColumnInfo(name = "cause") var cause: String
)
