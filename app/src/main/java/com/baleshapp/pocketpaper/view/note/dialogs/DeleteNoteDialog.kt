package com.baleshapp.pocketpaper.view.note.dialogs

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.Note

class DeleteNoteDialog(
    context: Context,
    private val note: Note,
    private val onDelete: (note: Note) -> Unit
) : AlertDialog(context, R.style.app_alert_dialog_style) {


    private val cancelWarningMessage =
        context.resources.getString(R.string.cancel_warning_message)
    private val deleteMessage = context.resources.getString(R.string.delete)
    private val cancelMessage = context.resources.getString(R.string.cancel)
    private val deleteNoteMessage = context.resources.getString(R.string.delete_note)
    private val deletedMessage = context.resources.getString(R.string.deleted)

    init {
        createDialog()
    }

    private fun createDialog() {
        val builder = Builder(context, R.style.app_alert_dialog_style)
        builder.setTitle("$deleteNoteMessage \"${note.name}\"?")
            .setMessage(cancelWarningMessage)
            .setPositiveButton(
                deleteMessage
            ) { _, _ -> deleteTask() }
            .setNegativeButton(
                cancelMessage
            ) { dialog, _ -> dialog.cancel() }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun deleteTask() {
        onDelete(note)
        Toast.makeText(context, deletedMessage, Toast.LENGTH_SHORT).show()
    }

}