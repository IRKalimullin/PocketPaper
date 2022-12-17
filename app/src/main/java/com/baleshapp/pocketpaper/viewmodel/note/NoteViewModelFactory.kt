package com.baleshapp.pocketpaper.viewmodel.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.baleshapp.pocketpaper.data.repository.NoteRepository

class NoteViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoteViewModel(repository) as T
    }
}