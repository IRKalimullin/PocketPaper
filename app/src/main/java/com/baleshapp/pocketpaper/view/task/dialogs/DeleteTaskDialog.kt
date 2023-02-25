package com.baleshapp.pocketpaper.view.task.dialogs

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.Task

class DeleteTaskDialog(
    context: Context,
    private val task: Task,
    private val onDelete: (task: Task) -> Unit
) : AlertDialog(context) {

    private val deleteTaskMessage =
        context.resources.getString(R.string.delete_task)
    private val deleteMessage = context.resources.getString(R.string.delete)
    private val cancelMessage = context.resources.getString(R.string.cancel)
    private val cancelWarningMessage =
        context.resources.getString(R.string.cancel_warning_message)
    private val deletedMessage = context.resources.getString(R.string.deleted)

    init {
        createDialog()
    }

    private fun createDialog(){
        val builder = Builder(context, R.style.app_alert_dialog_style)
        builder.setTitle("$deleteTaskMessage \"${task.name}\"?")
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
        onDelete(task)
        Toast.makeText(context, deletedMessage, Toast.LENGTH_SHORT).show()
    }
}