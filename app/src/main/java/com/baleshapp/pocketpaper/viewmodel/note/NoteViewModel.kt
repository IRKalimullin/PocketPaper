package com.baleshapp.pocketpaper.viewmodel.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.baleshapp.pocketpaper.data.model.Note
import com.baleshapp.pocketpaper.data.repository.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    fun getNotes(): LiveData<List<Note>> {
        return repository.getNotes().asLiveData()
    }

    fun insert(note: Note) = viewModelScope.launch {
        repository.insert(note)
    }

    fun update(note: Note) = viewModelScope.launch {
        repository.update(note)
    }

    fun delete(note: Note) = viewModelScope.launch {
        repository.delete(note)
    }

}