package com.baleshapp.pocketpaper.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.baleshapp.pocketpaper.data.database.converters.TaskTagConverter
import com.baleshapp.pocketpaper.data.model.*

@TypeConverters(TaskTagConverter::class)
@Database(
    entities = [Task::class, Note::class, PurchaseList::class, PurchaseItem::class, FinancialReport::class, FinanceItem::class, Habit::class, HabitPoint::class],
    version = 2, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun noteDao(): NoteDao
    abstract fun purchaseDao(): PurchaseDao
    abstract fun financeDao(): FinanceDao
    abstract fun habitDao(): HabitDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "pocket_paper_database"
                    )
                        .addMigrations(MIGRATION_1_2)
                        .build()
                }
            }
            return INSTANCE
        }
    }
}

internal val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Task ADD description TEXT DEFAULT '' NOT NULL")

        database.execSQL(
            "CREATE TABLE Habit (id INTEGER NOT NULL, name TEXT NOT NULL," +
                    " description TEXT NOT NULL, startDate INTEGER NOT NULL, repetition INTEGER NOT NULL," +
                    " isCheckable INTEGER NOT NULL, PRIMARY KEY(id))"
        )

        database.execSQL(
            "CREATE TABLE HabitPoint (id INTEGER NOT NULL, habitId INTEGER NOT NULL, " +
                    "isDone INTEGER NOT NULL, value INTEGER NOT NULL, date INTEGER NOT NULL, PRIMARY KEY(id))"
        )
    }
}