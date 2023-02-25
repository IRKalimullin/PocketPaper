package com.baleshapp.pocketpaper.view.task.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.Task
import com.baleshapp.pocketpaper.databinding.TaskDateTimePickeerLayoutBinding
import com.baleshapp.pocketpaper.utils.DateTimeUtil
import com.baleshapp.pocketpaper.view.calendar.CalendarActivity
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.Calendar

class TaskDateTimePickerDialog(
    context: Context,
    private val fragmentManager: FragmentManager,
    private var task: Task,
    private val onDateTimeSet: (date: Long, time: Long) -> Unit
) : AlertDialog(context) {

    var binding: TaskDateTimePickeerLayoutBinding
    private val cancelMessage = context.resources.getString(R.string.cancel)
    private val applyMessage = context.resources.getString(R.string.apply)
    var date = task.date
    var time = task.time
    var timeText = ""

    init {
        val inflater = LayoutInflater.from(context)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.task_date_time_pickeer_layout, null, false)
        binding.dialog = this
        createDialog()
    }

    private fun createDialog() {
        val builder = Builder(context, R.style.app_alert_dialog_style)
        builder.setView(binding.root)

        initCalendar()
        setTimeText(task.time)

        builder.setPositiveButton(
            applyMessage
        ) { _, _ -> onDateTimeSet(date, time) }
            .setNegativeButton(
                cancelMessage
            ) { dialog, _ -> dialog.cancel() }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun initCalendar() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = task.date

        val selectedDay = CalendarDay.from(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        binding.taskCalendarPicker.selectedDate = selectedDay
        binding.taskCalendarPicker.addDecorator(
            CalendarActivity.SelectedDayDecorator(
                selectedDay,
                context
            )
        )

        val dateTimeUtil = DateTimeUtil()
        binding.taskCalendarPicker.setOnDateChangedListener { _, date, _ ->
            this.date = dateTimeUtil.getDateLong(date.year, date.month - 1, date.day)
        }
    }

    private fun setTimeText(time: Long) {
        val dateTimeUtil = DateTimeUtil()
        timeText = if (time == 0L) {
            "-:-"
        } else {
            dateTimeUtil.getTimeString(time)
        }

        binding.invalidateAll()
    }

    fun createTimePicker() {
        val picker =
            MaterialTimePicker.Builder()
                .setTheme(R.style.app_time_picker_style)
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .build()
        picker.addOnPositiveButtonClickListener {
            val dateTimeUtil = DateTimeUtil()
            time = dateTimeUtil.getTimeLong(picker.hour, picker.minute)
            setTimeText(time)
        }
        picker.show(fragmentManager, "TIME")
    }
}