package com.baleshapp.pocketpaper.view.note.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.Note
import com.baleshapp.pocketpaper.databinding.NoteDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class NoteDialog(
    context: Context,
    private val isNewNote: Boolean,
    note: Note?,
    val onSave: (note: Note) -> Unit,
    val onUpdate: (note: Note) -> Unit
) : BottomSheetDialog(context) {

    val binding: NoteDialogBinding
    var note: Note
    lateinit var dialog: BottomSheetDialog

    private val untitledText = context.resources.getString(R.string.untitled)
    private val emptyNoteMessage = context.resources.getString(R.string.empty_note_text)

    init {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.note_dialog, null, false)
        if (note != null) {
            this.note = note
        } else {
            this.note = Note(
                name = "",
                text = "",
                isFavorite = false,
                timestampOfNote = System.currentTimeMillis()
            )
        }
        binding.dialog = this

        createDialog()
    }

    private fun createDialog() {
        dialog = BottomSheetDialog(context, R.style.bottom_sheet_dialog_style)
        binding.note = note
        dialog.setContentView(binding.root)
        dialog.show()
    }

    fun changeNote() {
        if (isValidate()) {
            if (isNewNote) {
                onSave(note)
            } else {
                onUpdate(note)
            }
            dialog.cancel()
        }
    }

    private fun isValidate(): Boolean {
        if (note.name.isEmpty()) {
            note.name = untitledText
        }
        return if (note.text.isEmpty()) {
            Toast.makeText(context, emptyNoteMessage, Toast.LENGTH_SHORT).show()
            false
        } else true
    }
}