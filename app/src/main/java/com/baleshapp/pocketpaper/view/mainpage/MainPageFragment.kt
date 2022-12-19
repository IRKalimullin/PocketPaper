package com.baleshapp.pocketpaper.view.mainpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.repository.TaskRepository
import com.baleshapp.pocketpaper.databinding.FragmentMainPageBinding
import com.baleshapp.pocketpaper.view.note.NoteListFragment
import com.baleshapp.pocketpaper.view.purchase.PurchaseFragment
import com.baleshapp.pocketpaper.view.task.adapters.TaskAdapter
import com.baleshapp.pocketpaper.view.task.dialogs.NewTaskDialog
import com.baleshapp.pocketpaper.viewmodel.task.TaskViewModel

import com.baleshapp.pocketpaper.viewmodel.task.TaskViewModelFactory

class MainPageFragment: Fragment() {

    private lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var taskViewModel: TaskViewModel
    lateinit var binding: FragmentMainPageBinding
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_page,container,false)

        binding.fragment = this

        viewModelFactory = TaskViewModelFactory(
            TaskRepository(binding.root.context)
        )

        taskViewModel = ViewModelProvider(this, viewModelFactory)[TaskViewModel::class.java]

        taskAdapter = TaskAdapter({
            taskViewModel.delete(it)
        }, {
            taskViewModel.update(it)
        })
        taskViewModel.getCurrentTasks(0).observe(viewLifecycleOwner, {
            taskAdapter.setItems(it)
        })
        val layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)

        binding.tasksRecyclerView.layoutManager = layoutManager
        binding.tasksRecyclerView.adapter = taskAdapter

        return binding.root
    }

    fun openNotesPage(){
        val fragmentManager = parentFragmentManager
        fragmentManager.beginTransaction().replace(R.id.main_container, NoteListFragment()).addToBackStack(null).commit()
    }

    fun openPurchasesPage(){
        val fragmentManager = parentFragmentManager
        fragmentManager.beginTransaction().replace(R.id.main_container, PurchaseFragment()).addToBackStack(null).commit()
    }

    fun createNewTask(){
        NewTaskDialog(binding.root.context) {
            taskViewModel.insert(it)
        }
    }
}