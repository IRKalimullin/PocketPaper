package com.baleshapp.pocketpaper.view.habit

import android.app.Activity
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.Habit
import com.baleshapp.pocketpaper.data.model.Note
import com.baleshapp.pocketpaper.data.repository.HabitRepository
import com.baleshapp.pocketpaper.databinding.ActivityHabitDetailBinding

import com.baleshapp.pocketpaper.utils.DateTimeUtil
import com.baleshapp.pocketpaper.view.note.NOTE_EXTRA_KEY
import com.baleshapp.pocketpaper.viewmodel.habit.HabitViewModel
import com.baleshapp.pocketpaper.viewmodel.habit.HabitViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*

class HabitDetailActivity : AppCompatActivity() {

    var habitStartDateText = ""
    var isNewHabit = true
    val dateTimeUtil = DateTimeUtil()
    lateinit var binding: ActivityHabitDetailBinding
    private lateinit var habitViewModel: HabitViewModel
    var habit: Habit? = null

    var isEditingMode = false

    lateinit var oldHabit: Habit

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

        oldHabit = habit as Habit

        val viewModelFactory = HabitViewModelFactory(
            HabitRepository(this)
        )

        habitViewModel = ViewModelProvider(this, viewModelFactory)[HabitViewModel::class.java]

        binding.habit = habit
        binding.activity = this

        habitStartDateText = dateTimeUtil.getDayName(habit!!.startDate, this)
        setContentView(binding.root)
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
        datePicker.show(supportFragmentManager,"DATE")
    }

    fun changeEditingMode() {
        if (isEditingMode) {
            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
        }
        isEditingMode = !isEditingMode
        binding.invalidateAll()
    }

    fun updateHabit() {
        habitViewModel.updateHabit(habit!!)
        changeEditingMode()
    }

    fun deleteHabit() {
        habitViewModel.fullDeleteHabit(habit!!)
        onBackPressed()
    }

    fun saveHabit() {
        if (isNewHabit) {
            habitViewModel.saveHabit(habit!!)
            isNewHabit = false
            binding.invalidateAll()
        }
    }
}