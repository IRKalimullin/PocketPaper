package com.baleshapp.pocketpaper.view.calendar

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.repository.TaskRepository
import com.baleshapp.pocketpaper.databinding.ActivityCalendarBinding
import com.baleshapp.pocketpaper.utils.DateTimeUtil
import com.baleshapp.pocketpaper.view.task.adapters.TaskListAdapter
import com.baleshapp.pocketpaper.viewmodel.task.TaskViewModel
import com.baleshapp.pocketpaper.viewmodel.task.TaskViewModelFactory
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView

class CalendarActivity : AppCompatActivity() {

    lateinit var binding: ActivityCalendarBinding
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var calendarView: MaterialCalendarView

    private var taskListAdapter: TaskListAdapter = TaskListAdapter(emptyList(), {
        taskViewModel.delete(it)
    }, {
        taskViewModel.update(it)
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = LayoutInflater.from(this)

        binding = DataBindingUtil.inflate(inflater, R.layout.activity_calendar, null, false)

        binding.activity = this

        val viewModelFactory = TaskViewModelFactory(
            TaskRepository(this)
        )
        taskViewModel = ViewModelProvider(this, viewModelFactory)[TaskViewModel::class.java]

        binding.selectedDateRecyclerView.adapter = taskListAdapter

        calendarView = binding.tasksCalendarMonth

        setContentView(binding.root)
        initCalendar()
    }

    private fun initCalendar() {
        var list = listOf<CalendarDay>()

        taskViewModel.getTasksDateList().observe(this) {
            list = taskViewModel.reformatDate(it)
            setDecorator(list, CalendarDay.today())
        }

        calendarView.setOnDateChangedListener { _, date, _ ->
            setData(date.year, date.month - 1, date.day)
            setDecorator(list, date)
        }
        setToday()
    }

    private fun setToday() {
        val today = CalendarDay.today()
        calendarView.selectedDate = today
        calendarView.currentDate = today
        calendarView.addDecorator(SelectedDayDecorator(today, this))
        setData(today.year, today.month - 1, today.day)
    }

    private fun setDecorator(datesList: List<CalendarDay>, selectedDay: CalendarDay) {
        calendarView.removeDecorators()
        calendarView.addDecorators(DaysDecorator(datesList, this))
        calendarView.addDecorator(SelectedDayDecorator(selectedDay, this))
        calendarView.invalidateDecorators()
    }

    private fun setData(year: Int, month: Int, dayOfMonth: Int) {
        val dates = DateTimeUtil().getDayBorders(year, month, dayOfMonth)
        taskViewModel.getTasksOnDate(dates[0], dates[1]).observe(this) {
            taskListAdapter.submitList(it)
        }
    }

    inner class DaysDecorator(dateList: Collection<CalendarDay>, private val context: Context) :
        DayViewDecorator {

        private val dates: HashSet<CalendarDay> = HashSet(dateList)

        override fun shouldDecorate(day: CalendarDay): Boolean {
            return dates.contains(day)
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(ForegroundColorSpan(context.getColor(R.color.green)))
        }
    }

    inner class SelectedDayDecorator(private val date: CalendarDay, private val context: Context) :
        DayViewDecorator {

        override fun shouldDecorate(day: CalendarDay): Boolean {
            return day == date
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(ForegroundColorSpan(context.getColor(R.color.white)))
        }
    }
}