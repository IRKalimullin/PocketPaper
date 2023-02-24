package com.baleshapp.pocketpaper.view.habit.dialog

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.Habit

class DeleteHabitDialog(
    context: Context,
    private val habit: Habit,
    private val onDelete: (habit: Habit) -> Unit
) : AlertDialog(context) {

    private val cancelWarningMessage =
        context.resources.getString(R.string.cancel_warning_message)
    private val deleteMessage = context.resources.getString(R.string.delete)
    private val cancelMessage = context.resources.getString(R.string.cancel)

    private val deleteHabitMessage = context.resources.getString(R.string.delete_habit)
    private val deletedMessage = context.resources.getString(R.string.deleted)

    init {
        createDialog()
    }

    private fun createDialog() {
        val builder = Builder(context, R.style.app_alert_dialog_style)
        builder.setTitle("$deleteHabitMessage \"${habit.name}\"?")
            .setMessage(cancelWarningMessage)
            .setPositiveButton(
                deleteMessage
            ) { _, _ -> deleteHabit() }
            .setNegativeButton(
                cancelMessage
            ) { dialog, _ -> dialog.cancel() }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun deleteHabit() {
        onDelete(habit)
        Toast.makeText(context, deletedMessage, Toast.LENGTH_SHORT).show()
    }
}