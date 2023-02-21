package com.baleshapp.pocketpaper.view.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.databinding.ActivityAllTasksBinding
import com.baleshapp.pocketpaper.view.task.alltasks.TasksPageFragment
import com.google.android.material.tabs.TabLayoutMediator

class AllTasksActivity : AppCompatActivity() {

    lateinit var binding: ActivityAllTasksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = LayoutInflater.from(this)

        binding = DataBindingUtil.inflate(inflater, R.layout.activity_all_tasks, null,false)

        binding.activity = this

        val activeTaskText = resources.getString(R.string.active_tasks)
        val completedTaskText = resources.getString(R.string.completed_tasks)
        val missedTaskText = resources.getString(R.string.missed_tasks)

        val adapter = FragmentViewPagerAdapter(this)

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

    inner class FragmentViewPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
        override fun getItemCount() = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> TasksPageFragment(position)
                1 -> TasksPageFragment(position)
                2 -> TasksPageFragment(position)
                else -> TasksPageFragment(0)
            }
        }

    }
}