package com.baleshapp.pocketpaper.view.note

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.Note
import com.baleshapp.pocketpaper.data.repository.NoteRepository
import com.baleshapp.pocketpaper.databinding.ActivityNoteListBinding
import com.baleshapp.pocketpaper.view.note.adapters.NoteAdapter
import com.baleshapp.pocketpaper.viewmodel.note.NoteViewModel
import com.baleshapp.pocketpaper.viewmodel.note.NoteViewModelFactory

const val NOTE_EXTRA_KEY = "NOTE_EXTRA_KEY"

class NoteListActivity : AppCompatActivity() {

    // NEW CLASS REDESIGN

    lateinit var binding: ActivityNoteListBinding
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var noteViewModel: NoteViewModel
    var isListEmpty = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = LayoutInflater.from(this)

        binding = DataBindingUtil.inflate(inflater, R.layout.activity_note_list, null, false)
        binding.activity = this

        setContentView(binding.root)

        gridLayoutManager = GridLayoutManager(this, 2)

        val viewModelFactory = NoteViewModelFactory(
            NoteRepository(binding.root.context)
        )

        noteViewModel = ViewModelProvider(this, viewModelFactory)[NoteViewModel::class.java]
        val adapter = NoteAdapter(
            { openNote(it) },
            { noteViewModel.delete(it) },
            { noteViewModel.update(it) })

        noteViewModel.getNotes().observe(this) {
            adapter.setItems(it)
            changeVisible(it.isEmpty())
        }

        binding.noteListRecyclerView.layoutManager = gridLayoutManager
        binding.noteListRecyclerView.adapter = adapter

    }

    private fun changeVisible(flag: Boolean) {
        isListEmpty = flag
        binding.invalidateAll()
    }

    fun createNewNote() {
        openNote(null)
    }

    private fun openNote(note: Note?) {
        val intent = Intent(this, NoteActivity::class.java)
        intent.putExtra(NOTE_EXTRA_KEY, note)
        startActivity(intent)
    }

}