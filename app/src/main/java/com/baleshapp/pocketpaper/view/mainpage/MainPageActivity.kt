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
import com.baleshapp.pocketpaper.view.task.adapters.TaskAdapter
import com.baleshapp.pocketpaper.view.task.adapters.TaskListAdapter
import com.baleshapp.pocketpaper.view.task.dialogs.NewTaskDialog
import com.baleshapp.pocketpaper.viewmodel.task.TaskViewModel
import com.baleshapp.pocketpaper.viewmodel.task.TaskViewModelFactory

class MainPageActivity : AppCompatActivity() {
    //NEW CLASS REDESIGN
    lateinit var popupMenu2: PopupMenu
    var isCompletedListEmpty = true
    var isTaskListEmpty = true
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskListAdapter: TaskListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(this)
        val binding = DataBindingUtil.inflate<ActivityMainPageBinding>(
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

        taskViewModel.getCurrentTasks().observe(this) {
            if (it.isNotEmpty()) {
                isTaskListEmpty = false
                binding.invalidateAll()
                taskListAdapter.setData(it)
            }else{
                isTaskListEmpty = true
                binding.invalidateAll()

            }

        }

        val layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        binding.todayActiveTaskListRv.layoutManager = layoutManager
        binding.todayActiveTaskListRv.adapter = taskListAdapter
    }

    fun openTagMenu() {
        popupMenu2.show()
    }

    fun createNewTask() {
        NewTaskDialog(this){taskViewModel.insert(it)}
    }

}