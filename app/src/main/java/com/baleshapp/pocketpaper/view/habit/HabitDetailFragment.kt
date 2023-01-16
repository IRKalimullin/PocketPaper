package com.baleshapp.pocketpaper.view.habit

import android.content.Context
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.Habit
import com.baleshapp.pocketpaper.data.model.HabitPoint
import com.baleshapp.pocketpaper.data.repository.HabitRepository
import com.baleshapp.pocketpaper.databinding.FragmentHabitDetailBinding
import com.baleshapp.pocketpaper.utils.DateTimeUtil
import com.baleshapp.pocketpaper.viewmodel.habit.HabitViewModel
import com.baleshapp.pocketpaper.viewmodel.habit.HabitViewModelFactory
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class HabitDetailFragment(private val habit: Habit) : Fragment() {

    lateinit var binding: FragmentHabitDetailBinding
    private lateinit var habitViewModel: HabitViewModel
    private lateinit var mContext: Context
    private var missedHabitPoints: List<CalendarDay> = listOf()
    private var completedHabitPoints: List<CalendarDay> = listOf()
    private val todayDate = CalendarDay.today()
    private val dateTimeUtil = DateTimeUtil()
    private var changedMessage = ""
    private var oldNotChangeMessage = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_habit_detail, container, false)

        mContext = binding.root.context
        changedMessage = mContext.resources.getString(R.string.changed)
        oldNotChangeMessage = mContext.resources.getString(R.string.old_not_change)

        if (habit.description == ""){
            habit.description = mContext.resources.getString(R.string.no_description)
        }

        binding.habit = habit
        binding.fragment = this

        val viewModelFactory = HabitViewModelFactory(
            HabitRepository(mContext)
        )

        habitViewModel = ViewModelProvider(this, viewModelFactory)[HabitViewModel::class.java]

        habitViewModel.getHabitPointList(habit.id).observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                habitViewModel.createStartDatePoint(habit)
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

        return binding.root
    }

    private fun setMissedHabitPoint(date: CalendarDay) {
        val selectedDate = dateTimeUtil.getDateLong(date.year, date.month - 1, date.day)
        var isChanged = false

        habitViewModel.getHabitPointOnDate(selectedDate, habit.id).observe(viewLifecycleOwner) {
            if (!isChanged) {
                if (it != null) {
                    val point = it
                    point.isDone = false
                    habitViewModel.updateHabitPoint(point)
                }
                isChanged = true
            }
        }

        Toast.makeText(mContext, changedMessage, Toast.LENGTH_SHORT).show()
    }

    private fun setCompletedHabitPoint(date: CalendarDay) {
        if ((date == todayDate) or (date.isAfter(todayDate))) {
            val selectedDate = dateTimeUtil.getDateLong(date.year, date.month - 1, date.day)

            var isChanged = false

            habitViewModel.getHabitPointOnDate(selectedDate, habit.id)
                .observe(viewLifecycleOwner) {
                    val habitPoint = it

                    if (!isChanged) {
                        if (habitPoint == null) {
                            val point = HabitPoint(
                                habitId = habit.id,
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
            Toast.makeText(mContext, oldNotChangeMessage, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun applyDecorators() {
        binding.habitCalendar.removeDecorators()
        binding.habitCalendar.addDecorators(
            HabitPointsDecorator(
                missedHabitPoints,
                mContext.getColor(R.color.crimson)
            )
        )
        binding.habitCalendar.addDecorators(
            HabitPointsDecorator(
                completedHabitPoints,
                mContext.getColor(R.color.green)
            )
        )
        binding.habitCalendar.addDecorators(
            TodayHabitPointDecorator(
                todayDate,
                mContext
            )
        )
        binding.habitCalendar.invalidateDecorators()
    }

    fun onBack() {
        val fragmentManager = parentFragmentManager
        fragmentManager.popBackStack()
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