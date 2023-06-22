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

internal val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE Folder (id INTEGER NOT NULL, name TEXT NOT NULL, PRIMARY KEY(id))"
        )

        database.execSQL(
            "CREATE TABLE CanceledTaskCause (id INTEGER NOT NULL, taskId INTEGER NOT NULL, " +
                    "cause TEXT NOT NULL, PRIMARY KEY(id))"
        )

        database.execSQL(
            "CREATE TABLE Task_backup (id INTEGER NOT NULL, name TEXT NOT NULL, " +
                    "isDone INTEGER NOT NULL, date INTEGER NOT NULL, time INTEGER NOT NULL, " +
                    "timestampOfTask INTEGER NOT NULL, description TEXT NOT NULL, tag TEXT NOT NULL)"
        )

        database.execSQL(
            "INSERT INTO Task_backup SELECT id, name, isDone, date, time, " +
                    "timestampOfTask, description,tag FROM Task"
        )

        database.execSQL("DROP TABLE Task")

        database.execSQL(
            "CREATE TABLE Task (id INTEGER NOT NULL, name TEXT NOT NULL, " +
                    "isDone INTEGER NOT NULL, date INTEGER NOT NULL, time INTEGER NOT NULL, " +
                    "timestampOfTask INTEGER NOT NULL, description TEXT NOT NULL, folderId INTEGER DEFAULT 0 NOT NULL," +
                    "priority TEXT DEFAULT 'NONE' NOT NULL, state TEXT DEFAULT 'CREATED' NOT NULL, PRIMARY KEY(id))"
        )

        database.execSQL(
            "INSERT INTO Task SELECT id, name, isDone, date, time, timestampOfTask," +
                    "description FROM Task_backup"
        )

        database.execSQL("DROP TABLE Task_backup")

        database.execSQL("ALTER TABLE Note ADD state DEFAULT 'CREATED' NOT NULL")
    }
}