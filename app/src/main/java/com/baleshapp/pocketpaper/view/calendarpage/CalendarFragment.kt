package com.baleshapp.pocketpaper.view.calendarpage

import android.content.Context
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.repository.TaskRepository
import com.baleshapp.pocketpaper.databinding.FragmentCalendarBinding
import com.baleshapp.pocketpaper.utils.DateTimeUtil
import com.baleshapp.pocketpaper.view.task.adapters.TaskAdapter
import com.baleshapp.pocketpaper.viewmodel.task.TaskViewModel
import com.baleshapp.pocketpaper.viewmodel.task.TaskViewModelFactory
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.time.ZoneId
import java.util.Calendar

class CalendarFragment : Fragment() {

    lateinit var binding: FragmentCalendarBinding
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var tasksAdapter: TaskAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var calendarView: MaterialCalendarView
    private lateinit var mContext: Context
    var isOpen = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false)

        mContext = binding.root.context

        val viewModelFactory = TaskViewModelFactory(
            TaskRepository(mContext)
        )

        binding.fragment = this
        layoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)

        taskViewModel = ViewModelProvider(this, viewModelFactory)[TaskViewModel::class.java]

        val cal: Calendar = Calendar.getInstance()
        setData(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))

        tasksAdapter = TaskAdapter({ taskViewModel.delete(it) }, { taskViewModel.update(it) })
        binding.selectedDateRecyclerView.layoutManager = layoutManager
        binding.selectedDateRecyclerView.adapter = tasksAdapter

        initCalendar()

        return binding.root
    }

    private fun initCalendar() {

        setCalendarView()

        val list = mutableListOf<CalendarDay>()

        taskViewModel.getTasksDateList().observe(viewLifecycleOwner) {

            for (i in it.indices) {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = it[i]
                val day = calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                list.add(CalendarDay.from(day.year, day.monthValue, day.dayOfMonth))
            }
            setDecorator(list)

            calendarView.addDecorator(SelectedDayDecorator(CalendarDay.today(), mContext))
        }

        calendarView.setOnDateChangedListener { _, date, _ ->
            setData(date.year, date.month - 1, date.day)
            setDecorator(list)
            calendarView.addDecorator(SelectedDayDecorator(date, mContext))
        }

        setToday()
    }

    private fun setCalendarView() {
        if (isOpen) {
            calendarView = binding.tasksCalendarMonth
            binding.tasksCalendarWeek.visibility = View.GONE
            calendarView.visibility = View.VISIBLE
            calendarView.invalidate()
        } else {
            calendarView = binding.tasksCalendarWeek
            binding.tasksCalendarMonth.visibility = View.GONE
            calendarView.visibility = View.VISIBLE
            calendarView.invalidate()
        }
    }

    fun changeMode() {
        isOpen = !isOpen
        binding.invalidateAll()
        initCalendar()
    }

    fun setToday() {
        calendarView.selectedDate = CalendarDay.today()
        calendarView.currentDate = CalendarDay.today()
        calendarView.addDecorator(SelectedDayDecorator(CalendarDay.today(), mContext))
    }

    private fun setDecorator(datesList: List<CalendarDay>) {
        calendarView.removeDecorators()
        val decorator = DaysDecorator(datesList, mContext)
        calendarView.addDecorators(decorator)
        calendarView.invalidateDecorators()
    }

    private fun setData(year: Int, month: Int, dayOfMonth: Int) {
        val dates = DateTimeUtil().getDayBorders(year, month, dayOfMonth)
        taskViewModel.getTasksOnDate(dates[0], dates[1]).observe(viewLifecycleOwner) {
            tasksAdapter.setItems(it)
        }
    }

    class DaysDecorator(dateList: Collection<CalendarDay>, private val context: Context) :
        DayViewDecorator {

        private val dates: HashSet<CalendarDay> = HashSet(dateList)

        override fun shouldDecorate(day: CalendarDay): Boolean {
            return dates.contains(day)
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(ForegroundColorSpan(context.getColor(R.color.green)))
        }
    }

    class SelectedDayDecorator(private val date: CalendarDay, private val context: Context) :
        DayViewDecorator {

        override fun shouldDecorate(day: CalendarDay): Boolean {
            return day == date
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(ForegroundColorSpan(context.getColor(R.color.white)))
        }
    }

}