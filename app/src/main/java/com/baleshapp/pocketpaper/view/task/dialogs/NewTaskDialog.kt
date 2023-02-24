package com.baleshapp.pocketpaper.view.task.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.Task
import com.baleshapp.pocketpaper.data.model.TaskTag
import com.baleshapp.pocketpaper.databinding.DialogNewTaskBinding
import com.baleshapp.pocketpaper.utils.DateTimeUtil
import com.google.android.material.bottomsheet.BottomSheetDialog

class NewTaskDialog(
    context: Context,
    private val fragmentManager: FragmentManager,
    val onSave: (task: Task) -> Unit
) : BottomSheetDialog(context) {

    private lateinit var dialog: BottomSheetDialog
    private val dateTimeUtil = DateTimeUtil()
    var dateText: String

    private val binding: DialogNewTaskBinding
    var isDescription = false

    private val emptyNameMessage = context.resources.getString(R.string.empty_name)

    var task = Task(
        name = "",
        isDone = false,
        time = 0L,
        date = dateTimeUtil.getTodayDate(),
        description = "",
        timestampOfTask = System.currentTimeMillis(),
        tag = TaskTag.GENERAL
    )

    init {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_new_task, null, false)
        binding.task = task
        binding.dialog = this
        dateText = context.resources.getString(R.string.today)
        createDialog()
    }

    private fun createDialog() {
        val inputManager: InputMethodManager = context.getSystemService()!!
        inputManager.showSoftInput(binding.root, InputMethodManager.SHOW_IMPLICIT)
        dialog = BottomSheetDialog(context, R.style.app_bottom_sheet_dialog_style)
        dialog.setContentView(binding.root)

        dialog.setOnCancelListener {
            inputManager.showSoftInput(binding.root, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }
        dialog.show()
        binding.taskInputName.requestFocus()
    }

    fun saveTask() {
        if (isValidated()) {
            task.tag = selectedTag(binding.taskInputTagGroup.checkedChipId)
            onSave(task)
            dialog.cancel()
        }
    }

    fun changeDescriptionState() {
        isDescription = !isDescription
        binding.invalidateAll()
    }

    private fun selectedTag(chipId: Int): TaskTag {
        return when (chipId) {
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

    private fun setDateTimeText(date: Long, time: Long) {
        var timeText = dateTimeUtil.getTimeString(time)
        timeText = if (time == 0L) {
            ""
        } else {
            "/ $timeText"
        }
        val dateText = dateTimeUtil.getDayName(date, binding.root.context)
        this.dateText = "$dateText $timeText"
        binding.invalidateAll()
    }

    private fun setDateTime(date: Long, time: Long) {
        task.date = date
        task.time = time
        setDateTimeText(date, time)
    }

    fun createDateTimePicker() {
        TaskDateTimePickerDialog(
            binding.root.context,
            fragmentManager,
            task
        ) { date: Long, time: Long ->
            setDateTime(date, time)
        }
    }
}