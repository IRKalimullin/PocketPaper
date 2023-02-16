package com.baleshapp.pocketpaper.view.mainpage

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.repository.TaskRepository
import com.baleshapp.pocketpaper.databinding.ActivityMainPageBinding
import com.baleshapp.pocketpaper.view.task.adapters.TaskListAdapter
import com.baleshapp.pocketpaper.view.task.dialogs.NewTaskDialog
import com.baleshapp.pocketpaper.viewmodel.task.TaskViewModel
import com.baleshapp.pocketpaper.viewmodel.task.TaskViewModelFactory

class MainPageActivity : AppCompatActivity() {
    //NEW CLASS REDESIGN
    lateinit var popupMenu2: PopupMenu
    var isCompletedVisible = false
    var isActiveVisible = false
    var isEmptyMessageVisible = true
    var isCompletedMessageVisible = false

    var isAempty = true
    var isCempty = true

    var isCompletedListEmpty = true
    var isTaskListEmpty = true
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskListAdapter: TaskListAdapter
    private lateinit var taskListAdapter2: TaskListAdapter
    lateinit var binding: ActivityMainPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(this)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.activity_main_page,
            null,
            false
        )
        binding.activity = this
        setContentView(binding.root)
        popupMenu2 = PopupMenu(this, binding.tagSelectorButton)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu2.setForceShowIcon(true)
        }
        popupMenu2.inflate(R.menu.task_tag_choice_menu)


        val viewModelFactory = TaskViewModelFactory(
            TaskRepository(this)
        )

        taskViewModel = ViewModelProvider(this, viewModelFactory)[TaskViewModel::class.java]

        taskListAdapter =
            TaskListAdapter({
                taskViewModel.delete(it)
            }, {
                taskViewModel.update(it)
            })

        taskListAdapter2 =
            TaskListAdapter({
                taskViewModel.delete(it)
            }, {
                taskViewModel.update(it)
            })


        taskViewModel.getCurrentCompletedTasks().observe(this) {
            if (it.isNotEmpty()) {
                taskListAdapter2.setData(it)
                taskListAdapter2.notifyDataSetChanged()
            }
            isCempty = it.isEmpty()
            setVisibilities()
        }

        taskViewModel.getCurrentActiveTasks().observe(this) {
            if (it.isNotEmpty()) {
                taskListAdapter.setData(it)
                taskListAdapter.notifyDataSetChanged()
            }
            isAempty = it.isEmpty()
            setVisibilities()

        }


        val layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val layoutManager2 =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        binding.todayActiveTaskListRv.layoutManager = layoutManager
        binding.todayActiveTaskListRv.adapter = taskListAdapter

        binding.todayCompletedTaskListRv.layoutManager = layoutManager2
        binding.todayCompletedTaskListRv.adapter = taskListAdapter2
    }

    fun setVisibilities() {
        if ((!isAempty) and (isCempty)) {
            isActiveVisible = true
            isCompletedVisible = false
            isEmptyMessageVisible = false
            isCompletedMessageVisible = false
        } else
            if ((!isAempty) and (!isCempty)) {
                isActiveVisible = true
                isCompletedVisible = true
                isEmptyMessageVisible = false
                isCompletedMessageVisible = false
            } else
                if ((isAempty) and (!isCempty)) {
                    isActiveVisible = false
                    isCompletedVisible = true
                    isEmptyMessageVisible = false
                    isCompletedMessageVisible = true
                } else
                    if ((isAempty) and (isCempty)) {
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

    fun createNewTask() {
        NewTaskDialog(this) { taskViewModel.insert(it) }
    }

}