package com.baleshapp.pocketpaper.view.task

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.repository.TaskRepository
import com.baleshapp.pocketpaper.databinding.FragmentCurrentTasksBinding

import com.baleshapp.pocketpaper.utils.DateTimeUtil
import com.baleshapp.pocketpaper.view.task.dialogs.NewTaskDialog
import com.baleshapp.pocketpaper.viewmodel.task.TaskViewModel
import com.baleshapp.pocketpaper.viewmodel.task.TaskViewModelFactory

class CurrentTasksFragment : Fragment() {

    var title: String = ""
    var date: String = System.currentTimeMillis().toString()
    val dateTimeUtil = DateTimeUtil()
    private lateinit var mContext: Context
    private lateinit var binding: FragmentCurrentTasksBinding
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var todayText: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_current_tasks,
            container,
            false
        )
        binding.currentTaskFragment = this

        val view = binding.root
        mContext = view.context

        val viewModelFactory = TaskViewModelFactory(
            TaskRepository(binding.root.context)
        )

        taskViewModel = ViewModelProvider(this, viewModelFactory)[TaskViewModel::class.java]

        val viewPagerAdapter = TaskViewPagerAdapter(this)
        binding.currentTasksViewPager.adapter = viewPagerAdapter

        binding.currentTasksViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                date = dateTimeUtil.getStringDateOfPosition(position)
                title = setPageName(position)
                binding.invalidateAll()
                viewPagerAdapter.createFragment(position)
            }
        })

        todayText = view.context.resources.getString(R.string.today)
        return view
    }

    fun createNewTask() {
        NewTaskDialog(mContext) {
            taskViewModel.insert(it)
        }
    }

    fun setPageName(position: Int): String {
        val name = dateTimeUtil.getDayNameOfPosition(position)
        return if (position == 0) {
            "$name ($todayText)"
        } else {
            name
        }
    }
}

class TaskViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 7
    }

    override fun createFragment(position: Int): Fragment {
        return TaskListFragment(position)
    }

}