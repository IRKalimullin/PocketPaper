package com.baleshapp.pocketpaper.view.task.dialogs

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
import com.baleshapp.pocketpaper.data.model.TaskTag
import com.baleshapp.pocketpaper.databinding.DialogCreateNewTaskBinding
import com.baleshapp.pocketpaper.utils.DateTimeUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*

class NewTaskDialog(
    context: Context,
    val onSave: (task: Task) -> Unit
) : BottomSheetDialog(context) {

    private lateinit var dialog: BottomSheetDialog
    private val dateTimeUtil = DateTimeUtil()
    var dateText: String
    var timeText: String
    private val binding: DialogCreateNewTaskBinding

    private val emptyNameMessage = context.resources.getString(R.string.empty_name)

    var task = Task(
        name = "",
        isDone = false,
        time = 0L,
        date = System.currentTimeMillis(),
        timestampOfTask = System.currentTimeMillis(),
        tag = TaskTag.GENERAL
    )

    init {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_create_new_task, null, false)
        binding.task = task
        binding.dialog = this
        dateText = context.resources.getString(R.string.today)
        timeText = context.resources.getString(R.string.time)
        createDialog()
    }

    private fun createDialog() {
        val inputManager: InputMethodManager = context.getSystemService()!!
        inputManager.showSoftInput(binding.root,InputMethodManager.SHOW_IMPLICIT)
        dialog = BottomSheetDialog(context, R.style.bottom_sheet_dialog_style)
        dialog.setContentView(binding.root)

        dialog.setOnCancelListener {
            inputManager.showSoftInput(binding.root,InputMethodManager.HIDE_IMPLICIT_ONLY)
        }
        dialog.show()
        binding.taskAddInputName.requestFocus()
    }

    fun saveTask() {
        if (isValidated()) {
            task.tag = selectedTag(binding.taskInputTag.checkedChipId)
            onSave(task)
            dialog.cancel()
        }
    }

    private fun selectedTag(chipId: Int): TaskTag{
        return when (chipId){
            R.id.general_tag_chip -> TaskTag.GENERAL
            R.id.personal_tag_chip -> TaskTag.PERSONAL
            R.id.work_tag_chip -> TaskTag.WORK
            R.id.study_tag_chip -> TaskTag.STUDY
            else -> TaskTag.GENERAL
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
            context, android.R.style.Theme_DeviceDefault_Dialog_Alert,
            { _, year, month, dayOfMonth ->
                task.date = dateTimeUtil.getDateLong(year, month, dayOfMonth)
                dateText = dateTimeUtil.getDayName(task.date,context)
                binding.invalidateAll()
            }, year, month, day
        ).show()
    }

    fun createTimeDialog() {
        TimePickerDialog(
            context, android.R.style.Theme_DeviceDefault_Dialog_Alert,
            { _, hourOfDay, minute ->
                task.time = dateTimeUtil.getTimeLong(hourOfDay, minute)
                timeText = dateTimeUtil.getTimeString(task.time)
                binding.invalidateAll()
            }, 12, 0, true
        ).show()
    }
}