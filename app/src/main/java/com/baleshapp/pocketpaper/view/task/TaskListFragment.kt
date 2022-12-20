package com.baleshapp.pocketpaper.view.task

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.Task
import com.baleshapp.pocketpaper.data.repository.TaskRepository
import com.baleshapp.pocketpaper.databinding.FragmentTaskPage1Binding
import com.baleshapp.pocketpaper.view.task.adapters.TaskAdapter
import com.baleshapp.pocketpaper.viewmodel.task.TaskViewModel
import com.baleshapp.pocketpaper.viewmodel.task.TaskViewModelFactory

class TaskListFragment(private val pageCount: Int) : Fragment() {

    private lateinit var fragmentContext: Context
    private lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var binding: FragmentTaskPage1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_task_page1, container, false)
        binding.pageFragment = this
        fragmentContext = binding.root.context

        viewModelFactory = TaskViewModelFactory(
            TaskRepository(fragmentContext)
        )

        taskViewModel = ViewModelProvider(this, viewModelFactory)[TaskViewModel::class.java]

        taskAdapter = TaskAdapter({
            taskViewModel.delete(it)
        }, {
            taskViewModel.update(it)
        })

        taskViewModel.getCurrentTasks(pageCount).observe(viewLifecycleOwner, {
            updateTaskContainer(it)
        })

        val layoutManager = LinearLayoutManager(fragmentContext, RecyclerView.VERTICAL, false)
        binding.currentTasksRecyclerView.layoutManager = layoutManager
        binding.currentTasksRecyclerView.adapter = taskAdapter

        return binding.root
    }

    private fun updateTaskContainer(taskList: List<Task>) {
        if (taskList.isEmpty()) {
            binding.emptyTaskListMsg.visibility = View.VISIBLE
            binding.currentTasksRecyclerView.visibility = View.GONE
        } else {
            binding.emptyTaskListMsg.visibility = View.GONE
            binding.currentTasksRecyclerView.visibility = View.VISIBLE
            taskAdapter.setItems(taskList)
        }
        binding.invalidateAll()
    }
}