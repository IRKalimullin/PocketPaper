package com.baleshapp.pocketpaper.view.mainpage

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.Task
import com.baleshapp.pocketpaper.data.model.TaskTag
import com.baleshapp.pocketpaper.data.repository.TaskRepository
import com.baleshapp.pocketpaper.databinding.ActivityMainPageBinding
import com.baleshapp.pocketpaper.settings.AppSettings
import com.baleshapp.pocketpaper.view.SettingsActivity
import com.baleshapp.pocketpaper.view.calendar.CalendarActivity
import com.baleshapp.pocketpaper.view.habit.HabitListActivity
import com.baleshapp.pocketpaper.view.note.NoteListActivity
import com.baleshapp.pocketpaper.view.purchase.PurchaseListActivity
import com.baleshapp.pocketpaper.view.task.alltasks.AllTasksActivity
import com.baleshapp.pocketpaper.view.task.TASK_EXTRA_KEY
import com.baleshapp.pocketpaper.view.task.TaskDetailActivity
import com.baleshapp.pocketpaper.view.task.adapters.TaskListAdapter
import com.baleshapp.pocketpaper.view.task.dialogs.NewTaskDialog
import com.baleshapp.pocketpaper.viewmodel.task.TaskViewModel
import com.baleshapp.pocketpaper.viewmodel.task.TaskViewModelFactory

class MainPageActivity : AppCompatActivity() {

    private lateinit var popupMenu2: PopupMenu
    var isCompletedVisible = false
    var isActiveVisible = false
    var isEmptyMessageVisible = true
    var isCompletedMessageVisible = false
    var isFirstStart = true
    private var isActiveEmpty = true
    private var isCompletedEmpty = true
    private lateinit var taskViewModel: TaskViewModel
    lateinit var binding: ActivityMainPageBinding
    private lateinit var appSettings: AppSettings

    private var taskListAdapter: TaskListAdapter = TaskListAdapter(emptyList(),
        {
            openTaskDetail(it)
        }, {
            taskViewModel.delete(it)
        }, {
            taskViewModel.update(it)
        })

    private var taskListAdapter2: TaskListAdapter = TaskListAdapter(emptyList(),
        {
            openTaskDetail(it)
        }, {
            taskViewModel.delete(it)
        }, {
            taskViewModel.update(it)
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main_page)
        binding.activity = this
        appSettings = AppSettings(this)
        isFirstStart = appSettings.isFirstStart()

        setContentView(binding.root)

        popupMenu2 = PopupMenu(this, binding.tagSelectorButton)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu2.setForceShowIcon(true)
        }
        popupMenu2.inflate(R.menu.task_tag_choice_menu)
        popupMenu2.setOnMenuItemClickListener {
            popUpSelectedItem(it)
        }

        val viewModelFactory = TaskViewModelFactory(
            TaskRepository(this)
        )

        taskViewModel = ViewModelProvider(this, viewModelFactory)[TaskViewModel::class.java]
        setAllTaskData()

        binding.todayActiveTaskListRv.adapter = taskListAdapter
        binding.todayCompletedTaskListRv.adapter = taskListAdapter2
    }

    private fun popUpSelectedItem(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.all_tasks_tag -> {
                setAllTaskData()
                true
            }

            R.id.general_tasks_tag -> {
                setTaskFilteredData(TaskTag.GENERAL)
                true
            }

            R.id.personal_tasks_tag -> {
                setTaskFilteredData(TaskTag.PERSONAL)
                true
            }

            R.id.work_tasks_tag -> {
                setTaskFilteredData(TaskTag.WORK)
                true
            }

            R.id.study_tasks_tag -> {
                setTaskFilteredData(TaskTag.STUDY)
                true
            }

            else -> false
        }
    }

    private fun setTaskFilteredData(taskTag: TaskTag) {
        taskViewModel.getCurrentCompletedTasks().removeObservers(this)
        taskViewModel.getCurrentActiveTasks().removeObservers(this)

        taskViewModel.getCurrentCompletedFilteredTasks(taskTag).observe(this) {
            if (it.isNotEmpty()) {
                taskListAdapter2.submitList(it)
            }
            isCompletedEmpty = it.isEmpty()
            setVisibilities()
        }

        taskViewModel.getCurrentActiveFilteredTasks(taskTag).observe(this) {
            if (it.isNotEmpty()) {
                taskListAdapter.submitList(it)
            }
            isActiveEmpty = it.isEmpty()
            setVisibilities()
        }
    }

    private fun setAllTaskData() {
        taskViewModel.getCurrentCompletedTasks().observe(this) {
            if (it.isNotEmpty()) {
                taskListAdapter2.submitList(it)
            }
            isCompletedEmpty = it.isEmpty()
            setVisibilities()
        }

        taskViewModel.getCurrentActiveTasks().observe(this) {
            if (it.isNotEmpty()) {
                taskListAdapter.submitList(it)
            }
            isActiveEmpty = it.isEmpty()
            setVisibilities()
        }
    }

    private fun setVisibilities() {
        if ((!isActiveEmpty) and (isCompletedEmpty)) {
            isActiveVisible = true
            isCompletedVisible = false
            isEmptyMessageVisible = false
            isCompletedMessageVisible = false
        } else
            if ((!isActiveEmpty) and (!isCompletedEmpty)) {
                isActiveVisible = true
                isCompletedVisible = true
                isEmptyMessageVisible = false
                isCompletedMessageVisible = false
            } else
                if ((isActiveEmpty) and (!isCompletedEmpty)) {
                    isActiveVisible = false
                    isCompletedVisible = true
                    isEmptyMessageVisible = false
                    isCompletedMessageVisible = true
                } else
                    if ((isActiveEmpty) and (isCompletedEmpty)) {
                        isActiveVisible = false
                        isCompletedVisible = false
                        isEmptyMessageVisible = true
                        isCompletedMessageVisible = false
                    }
        binding.invalidateAll()
    }

    override fun onResume() {
        super.onResume()
        binding.invalidateAll()
    }

    fun openTagMenu() {
        popupMenu2.show()
    }

    fun setFirstStarted() {
        isFirstStart = false
        appSettings.changeFirstStart()
        binding.invalidateAll()
    }

    private fun openTaskDetail(task: Task) {
        val intent = Intent(this, TaskDetailActivity::class.java).apply {
            putExtra(TASK_EXTRA_KEY, task)
        }
        startActivity(intent)
    }

    fun createNewTask() {
        NewTaskDialog(this, supportFragmentManager) { taskViewModel.insert(it) }
    }

    fun openNoteListActivity() {
        startActivity(Intent(this, NoteListActivity::class.java))
    }

    fun openPurchaseListActivity() {
        startActivity(Intent(this, PurchaseListActivity::class.java))
    }

    fun openHabitListActivity() {
        startActivity(Intent(this, HabitListActivity::class.java))
    }

    fun openCalendar() {
        startActivity(Intent(this, CalendarActivity::class.java))
    }

    fun openAllTasksList() {
        startActivity(Intent(this, AllTasksActivity::class.java))
    }

    fun openSettings() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }
}