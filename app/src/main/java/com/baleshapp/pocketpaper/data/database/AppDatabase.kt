package com.baleshapp.pocketpaper.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.baleshapp.pocketpaper.data.model.*

@Database(
    entities = [Task::class, Note::class, PurchaseList::class, PurchaseItem::class, FinancialReport::class, FinanceItem::class],
    version = 1, exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun noteDao(): NoteDao
    abstract fun purchaseDao(): PurchaseDao
    abstract fun financeDao(): FinanceDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "pocket_paper_database"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}