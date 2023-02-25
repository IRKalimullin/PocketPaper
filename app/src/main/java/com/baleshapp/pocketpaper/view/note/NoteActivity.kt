package com.baleshapp.pocketpaper.view.note

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.Note
import com.baleshapp.pocketpaper.data.repository.NoteRepository
import com.baleshapp.pocketpaper.databinding.ActivityNoteBinding
import com.baleshapp.pocketpaper.utils.ShareDataUtil
import com.baleshapp.pocketpaper.view.note.dialogs.DeleteNoteDialog
import com.baleshapp.pocketpaper.viewmodel.note.NoteViewModel
import com.baleshapp.pocketpaper.viewmodel.note.NoteViewModelFactory

class NoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteBinding
    var note: Note? = null
    private lateinit var noteViewModel: NoteViewModel
    private var untitledText = ""
    var isNewNote = true
    private lateinit var notePopupMenu: PopupMenu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_note)
        binding.activity = this
        binding.note = note
        binding.invalidateAll()
        setContentView(binding.root)

        untitledText = resources.getString(R.string.untitled)

        notePopupMenu = PopupMenu(this, binding.noteMenu)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            notePopupMenu.setForceShowIcon(true)
        }
        notePopupMenu.inflate(R.menu.note_edit_menu)
        notePopupMenu.setOnMenuItemClickListener {
            popUpSelectedItem(it)
        }
    }

    fun openMenu() {
        notePopupMenu.show()
    }

    private fun popUpSelectedItem(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_note -> {
                DeleteNoteDialog(this, note!!) { deleteNote(it) }
                true
            }

            R.id.share_note -> {
                ShareDataUtil(this, note!!.text)
                true
            }

            else -> false
        }
    }

    private fun deleteNote(note: Note) {
        noteViewModel.delete(note)
        finish()
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
            isNewNote = false
            binding.invalidateAll()
        }
    }

    private fun isValidate(): Boolean {
        if (note!!.name.isEmpty() and note!!.text.isNotEmpty()) {
            note!!.name = untitledText
        }
        return if (note!!.text.isEmpty() and note!!.name.isEmpty()) {
            val message = resources.getString(R.string.empty_note_deleted)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            false
        } else true
    }
}