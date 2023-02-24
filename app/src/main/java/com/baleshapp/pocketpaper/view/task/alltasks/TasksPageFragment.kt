package com.baleshapp.pocketpaper.view.task.alltasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.Task
import com.baleshapp.pocketpaper.data.repository.TaskRepository
import com.baleshapp.pocketpaper.databinding.FragmentTasksPageBinding
import com.baleshapp.pocketpaper.view.task.adapters.TaskListAdapter
import com.baleshapp.pocketpaper.viewmodel.task.TaskViewModel
import com.baleshapp.pocketpaper.viewmodel.task.TaskViewModelFactory

class TasksPageFragment(
    private val position: Int,
    private val onOpenDetail: (task: Task) -> Unit
) : Fragment() {

    lateinit var binding: FragmentTasksPageBinding
    private lateinit var viewModelFactory: TaskViewModelFactory
    lateinit var viewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks_page, container, false)
        binding.fragment = this

        viewModelFactory = TaskViewModelFactory(
            TaskRepository(binding.root.context)
        )
        viewModel = ViewModelProvider(this, viewModelFactory)[TaskViewModel::class.java]

        val adapter =
            TaskListAdapter(
                emptyList(),
                onOpenDetail,
                { viewModel.delete(it) },
                { viewModel.update(it) })

        when (position) {
            0 -> viewModel.getActiveTasks().observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
            1 -> viewModel.getCompletedTasks().observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
            2 -> viewModel.getMissedTasks().observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }

        binding.currentTasksRecyclerView.adapter = adapter

        return binding.root
    }
}