package com.baleshapp.pocketpaper.view.habit.dialog

import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.Habit
import com.baleshapp.pocketpaper.databinding.DialogNewHabitBinding
import com.baleshapp.pocketpaper.utils.DateTimeUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*

class NewHabitDialog(
    context: Context,
    private val onSave: (habit: Habit) -> Unit
) :
    BottomSheetDialog(context) {

    val binding: DialogNewHabitBinding
    private lateinit var dialog: BottomSheetDialog
    private val dateTimeUtil = DateTimeUtil()
    var startDateText = context.resources.getString(R.string.today)

    var habit = Habit(
        name = "",
        description = "",
        startDate = dateTimeUtil.getTodayDate(),
        repetition = 0,
        isCheckable = true
    )

    init {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_new_habit, null, false)
        binding.dialog = this
        binding.habit = habit
        createDialog()
    }

    private fun createDialog() {
        val inputManager: InputMethodManager = context.getSystemService()!!
        dialog = BottomSheetDialog(context, R.style.bottom_sheet_dialog_style)
        dialog.setContentView(binding.root)
        dialog.setOnCancelListener {
            inputManager.showSoftInput(binding.root, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }
        dialog.show()
    }

    fun createDateDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(
            context, android.R.style.Theme_DeviceDefault_Dialog_Alert,
            { _, year, month, dayOfMonth ->
                habit.startDate = dateTimeUtil.getDateLong(year, month, dayOfMonth)
                startDateText = dateTimeUtil.getDayName(habit.startDate, context)
                binding.invalidateAll()
            }, year, month, day
        ).show()
    }

    fun saveHabit() {
        onSave(habit)
        dialog.cancel()
    }
}