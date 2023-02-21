package com.baleshapp.pocketpaper.view.note

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.Note
import com.baleshapp.pocketpaper.data.repository.NoteRepository
import com.baleshapp.pocketpaper.databinding.ActivityNoteBinding
import com.baleshapp.pocketpaper.viewmodel.note.NoteViewModel
import com.baleshapp.pocketpaper.viewmodel.note.NoteViewModelFactory

class NoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteBinding
    var note: Note? = null
    private lateinit var noteViewModel: NoteViewModel
    private var untitledText = ""
    private var emptyNoteMessage = ""
    var isNewNote = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = LayoutInflater.from(this)

        note = intent.getSerializableExtra(NOTE_EXTRA_KEY) as Note?

        if (note == null) {
            isNewNote = true
            note = Note(
                name = "",
                text = "",
                isFavorite = false,
                timestampOfNote = System.currentTimeMillis()
            )
        } else {
            isNewNote = false
        }

        val noteViewModelFactory = NoteViewModelFactory(
            NoteRepository(this)
        )

        noteViewModel = ViewModelProvider(this, noteViewModelFactory)[NoteViewModel::class.java]

        binding = DataBindingUtil.inflate(inflater, R.layout.activity_note, null, false)

        binding.activity = this
        binding.note = note

        binding.invalidateAll()

        setContentView(binding.root)

        untitledText = resources.getString(R.string.untitled)
        emptyNoteMessage = resources.getString(R.string.empty_note_text)

    }

    override fun onResume() {
        super.onResume()
        if (isNewNote) {
            binding.noteTextInput.requestFocus()
            showKeyboard()
        }
    }

    private fun showKeyboard() {
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    override fun onStop() {
        super.onStop()
        changeNote()
    }

    fun changeNote() {
        if (isValidate()) {
            if (isNewNote) {
                noteViewModel.insert(note!!)
            } else {
                noteViewModel.update(note!!)
            }
            binding.invalidateAll()
        }
    }

    private fun isValidate(): Boolean {
        if (note!!.name.isEmpty() and note!!.text.isNotEmpty()) {
            note!!.name = untitledText
        }
        return if (note!!.text.isEmpty() and note!!.name.isEmpty()) {
            Toast.makeText(this, "Пустая заметка удалена", Toast.LENGTH_SHORT).show()
            false
        } else true
    }
}