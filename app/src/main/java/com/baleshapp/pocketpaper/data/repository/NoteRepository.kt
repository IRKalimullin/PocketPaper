package com.baleshapp.pocketpaper.data.repository

import android.content.Context
import com.baleshapp.pocketpaper.data.database.AppDatabase
import com.baleshapp.pocketpaper.data.database.NoteDao
import com.baleshapp.pocketpaper.data.model.Note
import kotlinx.coroutines.flow.Flow

class NoteRepository(context: Context) {

    private val noteDao: NoteDao

    init {
        val database = AppDatabase.getDatabase(context)
        noteDao = database!!.noteDao()
    }

    fun getNotes(): Flow<List<Note>> {
        return noteDao.getNotes()
    }

    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    suspend fun update(note: Note) {
        noteDao.update(note)
    }

    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }

}