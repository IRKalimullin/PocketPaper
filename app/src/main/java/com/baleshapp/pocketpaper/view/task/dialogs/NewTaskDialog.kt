package com.baleshapp.pocketpaper.view.task.dialogs

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.Task
import com.baleshapp.pocketpaper.databinding.AddTaskLineBinding
import com.baleshapp.pocketpaper.utils.DateTimeUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*

class NewTaskDialog(
    context: Context,
    val onSave: (task: Task) -> Unit
) : BottomSheetDialog(context) {

    private lateinit var dialog: BottomSheetDialog
    private val dateTimeUtil = DateTimeUtil()
    var dateText: String = "Сегодня"
    var timeText: String = "Время"
    private val binding: AddTaskLineBinding

    private val emptyNameMessage = context.resources.getString(R.string.empty_name)

    var task = Task(
        name = "",
        isDone = false,
        time = 0L,
        date = System.currentTimeMillis(),
        timestampOfTask = System.currentTimeMillis()
    )

    init {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.add_task_line, null, false)
        binding.task = task
        binding.dialog = this
        createDialog()
    }

    private fun createDialog() {
        val inputManager: InputMethodManager = context.getSystemService()!!
        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        dialog = BottomSheetDialog(context, R.style.bottom_sheet_dialog_style)
        dialog.setContentView(binding.root)

        dialog.setOnCancelListener {
            inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        }
        dialog.show()
        binding.taskAddInputName.requestFocus()
    }

    fun saveTask() {
        if (isValidated()) {
            onSave(task)
            dialog.cancel()
        }
    }

    private fun isValidated(): Boolean {
        return if (task.name.isEmpty()) {
            Toast.makeText(context, emptyNameMessage, Toast.LENGTH_SHORT).show()
            return false
        } else true
    }

    fun createDateDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(
            context, AlertDialog.THEME_DEVICE_DEFAULT_DARK,
            { _, year, month, dayOfMonth ->
                task.date = dateTimeUtil.getDateLong(year, month, dayOfMonth)
                dateText = dateTimeUtil.getDayName(task.date)
                binding.invalidateAll()
            }, year, month, day
        ).show()
    }

    fun createTimeDialog() {
        TimePickerDialog(
            context, AlertDialog.THEME_DEVICE_DEFAULT_DARK,
            { _, hourOfDay, minute ->
                task.time = dateTimeUtil.getTimeLong(hourOfDay, minute)
                timeText = dateTimeUtil.getTimeString(task.time)
                binding.invalidateAll()
            }, 12, 0, true
        ).show()
    }
}