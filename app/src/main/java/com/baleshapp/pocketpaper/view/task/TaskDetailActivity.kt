package com.baleshapp.pocketpaper.view.task

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
import com.baleshapp.pocketpaper.databinding.ActivityTaskDetailBinding
import com.baleshapp.pocketpaper.utils.DateTimeUtil
import com.baleshapp.pocketpaper.view.task.dialogs.DeleteTaskDialog
import com.baleshapp.pocketpaper.view.task.dialogs.TaskDateTimePickerDialog
import com.baleshapp.pocketpaper.viewmodel.task.TaskViewModel
import com.baleshapp.pocketpaper.viewmodel.task.TaskViewModelFactory

const val TASK_EXTRA_KEY = "TASK_EXTRA_KEY"

class TaskDetailActivity : AppCompatActivity() {

    var tagText = ""
    private lateinit var popupMenu2: PopupMenu
    lateinit var binding: ActivityTaskDetailBinding
    var task: Task? = null
    var taskDateTimeText = ""
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_task_detail)
        task = intent.getSerializableExtra(TASK_EXTRA_KEY) as Task?
        binding.activity = this

        setTaskTag(task!!.tag)
        setDateTimeText(task!!)

        popupMenu2 = PopupMenu(this, binding.taskTagDetail)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu2.setForceShowIcon(true)
        }
        popupMenu2.inflate(R.menu.task_tag_choice_menu)
        popupMenu2.setOnMenuItemClickListener {
            popUpSelectedItem(it)
        }
        popupMenu2.menu.removeItem(R.id.all_tasks_tag)

        val viewModelFactory = TaskViewModelFactory(
            TaskRepository(this)
        )
        taskViewModel = ViewModelProvider(this, viewModelFactory)[TaskViewModel::class.java]

        binding.task = task
        setContentView(binding.root)
    }

    private fun setDateTimeText(task: Task){
        val dateTimeUtil = DateTimeUtil()

        val dateText = dateTimeUtil.getDateString(task.date)
        var timeText = dateTimeUtil.getTimeString(task.time)

        if (timeText == "04:00") {
            timeText = "-:-"
        }

        taskDateTimeText = "$dateText / $timeText"
        binding.invalidateAll()
    }

    fun openTaskTagMenu() {
        popupMenu2.show()
    }

    private fun setTaskTag(tag: TaskTag) {
        task!!.tag = tag
        tagText = getTagText(tag)
        binding.taskTagDetail.setChipBackgroundColorResource(getTagColor(task!!.tag))
        binding.invalidateAll()
    }

    fun openDateTimePicker() {
        TaskDateTimePickerDialog(
            this,
            supportFragmentManager,
            task!!
        ) { date: Long, time: Long -> setDateTime(date, time) }
    }

    private fun setDateTime(date: Long, time: Long) {
        task!!.date = date
        task!!.time = time
        setDateTimeText(task!!)
    }

    private fun popUpSelectedItem(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.general_tasks_tag -> {
                setTaskTag(TaskTag.GENERAL)
                true
            }

            R.id.personal_tasks_tag -> {
                setTaskTag(TaskTag.PERSONAL)
                true
            }

            R.id.work_tasks_tag -> {
                setTaskTag(TaskTag.WORK)
                true
            }

            R.id.study_tasks_tag -> {
                setTaskTag(TaskTag.STUDY)
                true
            }

            else -> false
        }
    }

    fun createDeleteDialog(){
        DeleteTaskDialog(this,task!!) { deleteTask(it) }
    }

    private fun deleteTask(task: Task){
        taskViewModel.delete(task)
        finish()
    }

    private fun getTagColor(tag: TaskTag): Int {
        return when (tag) {
            TaskTag.GENERAL -> R.color.wild_willow
            TaskTag.PERSONAL -> R.color.macaroni_and_cheese
            TaskTag.WORK -> R.color.malibu
            TaskTag.STUDY -> R.color.golden_tainoi
        }
    }

    private fun getTagText(tag: TaskTag): String {
        return when (tag) {
            TaskTag.GENERAL -> resources.getString(R.string.general_tag_text)
            TaskTag.PERSONAL -> resources.getString(R.string.personal_tag_text)
            TaskTag.WORK -> resources.getString(R.string.work_tag_text)
            TaskTag.STUDY -> resources.getString(R.string.study_tag_text)
        }
    }

    override fun onStop() {
        super.onStop()
        taskViewModel.update(task!!)
    }
}