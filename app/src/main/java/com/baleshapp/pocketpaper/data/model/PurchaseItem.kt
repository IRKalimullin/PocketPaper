package com.baleshapp.pocketpaper.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PurchaseItem(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "categoryId") var categoryId: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "number") var number: Int,
    @ColumnInfo(name = "isAdded") var isAdded: Boolean,
    @ColumnInfo(name = "timestampOfItem") var timestampOfItem: Long
)