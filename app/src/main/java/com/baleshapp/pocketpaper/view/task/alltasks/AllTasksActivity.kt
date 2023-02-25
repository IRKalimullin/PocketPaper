package com.baleshapp.pocketpaper.view.task.alltasks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.Task
import com.baleshapp.pocketpaper.databinding.ActivityAllTasksBinding
import com.baleshapp.pocketpaper.view.task.TASK_EXTRA_KEY
import com.baleshapp.pocketpaper.view.task.TaskDetailActivity
import com.google.android.material.tabs.TabLayoutMediator

class AllTasksActivity : AppCompatActivity() {

    lateinit var binding: ActivityAllTasksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_all_tasks)
        binding.activity = this

        val activeTaskText = resources.getString(R.string.active_tasks)
        val completedTaskText = resources.getString(R.string.completed_tasks)
        val missedTaskText = resources.getString(R.string.missed_tasks)

        val adapter = FragmentViewPagerAdapter(this) { openTaskDetail(it) }
        binding.allTasksViewPager.adapter = adapter

        TabLayoutMediator(binding.allTaskTabLayout, binding.allTasksViewPager) { tab, position ->
            tab.text = when (position) {
                1 -> completedTaskText
                2 -> missedTaskText
                else -> activeTaskText
            }
        }.attach()

        setContentView(binding.root)
    }

    private fun openTaskDetail(task: Task) {
        val intent = Intent(this, TaskDetailActivity::class.java).apply {
            putExtra(TASK_EXTRA_KEY, task)
        }
        startActivity(intent)
    }

    inner class FragmentViewPagerAdapter(
        fragment: FragmentActivity,
        private val onOpenDetail: (task: Task) -> Unit
    ) : FragmentStateAdapter(fragment) {
        override fun getItemCount() = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> TasksPageFragment(position, onOpenDetail)
                1 -> TasksPageFragment(position, onOpenDetail)
                2 -> TasksPageFragment(position, onOpenDetail)
                else -> TasksPageFragment(0, onOpenDetail)
            }
        }

    }
}