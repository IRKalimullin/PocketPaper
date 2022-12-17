package com.baleshapp.pocketpaper.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FinanceItem(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "currentFinancialReportId") var currentFinancialReportId: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "color") var color: Int,
    @ColumnInfo(name = "value") var value: Int,
    @ColumnInfo(name = "type") var type: TransactionType,
    @ColumnInfo(name = "timestampOfItem") var timestampOfFinance: Long
)

