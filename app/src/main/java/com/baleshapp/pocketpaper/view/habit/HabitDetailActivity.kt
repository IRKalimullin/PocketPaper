package com.baleshapp.pocketpaper.view.habit

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.Habit
import com.baleshapp.pocketpaper.data.model.HabitPoint
import com.baleshapp.pocketpaper.data.repository.HabitRepository
import com.baleshapp.pocketpaper.databinding.ActivityHabitDetailBinding
import com.baleshapp.pocketpaper.utils.DateTimeUtil
import com.baleshapp.pocketpaper.viewmodel.habit.HabitViewModel
import com.baleshapp.pocketpaper.viewmodel.habit.HabitViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class HabitDetailActivity : AppCompatActivity() {

    var habitStartDateText = ""
    var isNewHabit = true
    val dateTimeUtil = DateTimeUtil()
    lateinit var binding: ActivityHabitDetailBinding
    private lateinit var habitViewModel: HabitViewModel
    var habit: Habit? = null
    private val todayDate = CalendarDay.today()
    private var missedHabitPoints: List<CalendarDay> = listOf()
    private var completedHabitPoints: List<CalendarDay> = listOf()
    private var changedMessage = ""
    private var oldNotChangeMessage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = LayoutInflater.from(this)

        binding = DataBindingUtil.inflate(inflater, R.layout.activity_habit_detail, null, false)

        habit = intent.getSerializableExtra(HABIT_EXTRA_KEY) as Habit?

        if (habit == null) {
            isNewHabit = true
            habit = Habit(
                name = "",
                description = "",
                startDate = dateTimeUtil.getTodayDate(),
                repetition = 0,
                isCheckable = true
            )
        } else {
            isNewHabit = false
        }

        val viewModelFactory = HabitViewModelFactory(
            HabitRepository(this)
        )

        habitViewModel = ViewModelProvider(this, viewModelFactory)[HabitViewModel::class.java]

        binding.habit = habit
        binding.activity = this

        if (!isNewHabit) {
            setHabitPointsData()
        }

        habitStartDateText = dateTimeUtil.getDayName(habit!!.startDate, this)
        setContentView(binding.root)
    }

    private fun setHabitPointsData() {
        habitViewModel.getHabitPointList(habit!!.id).observe(this) {
            if (it.isEmpty()) {
                habitViewModel.createStartDatePoint(habit!!)
            } else {
                missedHabitPoints = habitViewModel.getMissedHabitPoints(it)
                completedHabitPoints = habitViewModel.getCompletedHabitPoints(it)

                applyDecorators()
            }
        }

        binding.habitCalendar.setOnDateChangedListener { _, date, _ ->
            setCompletedHabitPoint(date)
        }

        binding.habitCalendar.setOnDateLongClickListener { _, date ->
            setMissedHabitPoint(date)
        }
    }

    private fun setMissedHabitPoint(date: CalendarDay) {
        val selectedDate = dateTimeUtil.getDateLong(date.year, date.month - 1, date.day)
        var isChanged = false

        habitViewModel.getHabitPointOnDate(selectedDate, habit!!.id).observe(this) {
            if (!isChanged) {
                if (it != null) {
                    val point = it
                    point.isDone = false
                    habitViewModel.updateHabitPoint(point)
                }
                isChanged = true
            }
        }
        changedMessage = resources.getString(R.string.changed)

        Toast.makeText(this, changedMessage, Toast.LENGTH_SHORT).show()
    }

    private fun setCompletedHabitPoint(date: CalendarDay) {
        if ((date == todayDate) or (date.isAfter(todayDate))) {
            val selectedDate = dateTimeUtil.getDateLong(date.year, date.month - 1, date.day)

            var isChanged = false

            habitViewModel.getHabitPointOnDate(selectedDate, habit!!.id)
                .observe(this) {
                    val habitPoint = it

                    if (!isChanged) {
                        if (habitPoint == null) {
                            val point = HabitPoint(
                                habitId = habit!!.id,
                                isDone = true,
                                value = 0,
                                date = selectedDate
                            )
                            habitViewModel.insertHabitPoint(point)

                        } else {
                            habitPoint.isDone = true
                            habitViewModel.updateHabitPoint(habitPoint)
                        }
                        isChanged = true
                    }
                }

        } else {
            oldNotChangeMessage = resources.getString(R.string.old_not_change)
            Toast.makeText(this, oldNotChangeMessage, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun applyDecorators() {
        binding.habitCalendar.removeDecorators()
        binding.habitCalendar.addDecorators(
            HabitPointsDecorator(
                missedHabitPoints,
                this.getColor(R.color.crimson)
            )
        )
        binding.habitCalendar.addDecorators(
            HabitPointsDecorator(
                completedHabitPoints,
                this.getColor(R.color.green)
            )
        )
        binding.habitCalendar.addDecorators(
            TodayHabitPointDecorator(
                todayDate,
                this
            )
        )
        binding.habitCalendar.invalidateDecorators()
    }

    fun openDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTheme(R.style.app_date_picker_dialog_style)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.addOnPositiveButtonClickListener {
            habit!!.startDate = it
            habitStartDateText = dateTimeUtil.getDayName(habit!!.startDate, this)
            binding.invalidateAll()
        }
        datePicker.show(supportFragmentManager, "DATE")
    }

    fun deleteHabit() {
        habitViewModel.fullDeleteHabit(habit!!)
        onBackPressed()
    }

    fun saveHabit() {
        if (isNewHabit) {
            habitViewModel.saveHabit(habit!!)
            isNewHabit = false
            hideKeyboard()
            binding.invalidateAll()
            setHabitPointsData()
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    override fun onStop() {
        super.onStop()
        if (!isNewHabit) {
            habitViewModel.updateHabit(habit!!)
        }
    }

    class HabitPointsDecorator(
        private val dateList: Collection<CalendarDay>,
        private val color: Int
    ) :
        DayViewDecorator {

        override fun shouldDecorate(day: CalendarDay): Boolean {
            return dateList.contains(day)
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(DotSpan(10f, color))
            view.addSpan(ForegroundColorSpan(color))
        }
    }

    class TodayHabitPointDecorator(
        private val date: CalendarDay,
        private val context: Context
    ) :
        DayViewDecorator {

        override fun shouldDecorate(day: CalendarDay): Boolean {
            return date == day
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(ForegroundColorSpan(context.getColor(R.color.yellow)))
        }
    }
}