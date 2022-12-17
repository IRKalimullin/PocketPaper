package com.baleshapp.pocketpaper.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FinancialReport(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "spentTotalValue") var spentTotalValue: Int,
    @ColumnInfo(name = "receiveTotalValue") var receiveTotalValue: Int
)
