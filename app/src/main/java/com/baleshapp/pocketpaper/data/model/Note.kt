package com.baleshapp.pocketpaper.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "text") var text: String,
    @ColumnInfo(name = "isFavorite") var isFavorite: Boolean,
    @ColumnInfo(name = "timestampOfNote") var timestampOfNote: Long
) : java.io.Serializable


