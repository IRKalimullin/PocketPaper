package com.baleshapp.pocketpaper.view.task.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.Task
import com.baleshapp.pocketpaper.data.model.TaskTag
import com.baleshapp.pocketpaper.databinding.DialogTaskDetailsBinding
import com.baleshapp.pocketpaper.utils.DateTimeUtil
import com.google.android.material.bottomsheet.BottomSheetDialog

class TaskDetailDialog(
    context: Context,
    var task: Task,
    private val onDelete: (task: Task) -> Unit,
    private val onUpdate: (task: Task) -> Unit,
) : BottomSheetDialog(context) {

    private lateinit var dialog: BottomSheetDialog
    private val dateTimeUtil = DateTimeUtil()
    private var binding: DialogTaskDetailsBinding
    var dateText: String
    var timeText: String
    var isEditing = false
    private var oldTask: Task

    private val deletedMessage = context.resources.getString(R.string.deleted)

    init {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_task_details, null, false)

        oldTask = task
        binding.task = task
        binding.dialog = this
        setCheckedTag(task.tag)

        dateText = dateTimeUtil.getDateString(task.date)
        timeText = dateTimeUtil.getTimeString(task.time)
        if (timeText == "04:00") {
            timeText = "empty"
        }

        createDialog()
    }

    private fun createDialog() {
        dialog = BottomSheetDialog(context, R.style.bottom_sheet_dialog_style)
        dialog.setContentView(binding.root)
        dialog.show()
    }

    fun deleteTask() {
        onDelete(task)
        dialog.cancel()
        Toast.makeText(context, deletedMessage, Toast.LENGTH_SHORT).show()
    }

    fun switchEditingMode() {
        isEditing = !isEditing

        if (!isEditing) {
            task = oldTask
            val inputManager: InputMethodManager = binding.root.context.getSystemService()!!
            inputManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
            binding.taskDescriptionName.clearFocus()
            binding.taskDescriptionText.clearFocus()
        }
        binding.invalidateAll()
    }

    fun updateTask() {
        if (isEditing) {
            onUpdate(task)
            oldTask = task
            isEditing = !isEditing
            dialog.cancel()
        }
    }

    fun changeDoneState() {
        if (!task.isDone) {
            task.isDone = true
            onUpdate(task)
            dialog.dismiss()
            dialog.cancel()
        }
    }

    private fun setCheckedTag(taskTag: TaskTag) {
        when (taskTag) {
            TaskTag.GENERAL -> binding.generalTagChipDescription.isChecked = true
            TaskTag.STUDY -> binding.studyTagChipDescription.isChecked = true
            TaskTag.PERSONAL -> binding.personalTagChipDescription.isChecked = true
            TaskTag.WORK -> binding.workTagChipDescription.isChecked = true
        }
    }

}