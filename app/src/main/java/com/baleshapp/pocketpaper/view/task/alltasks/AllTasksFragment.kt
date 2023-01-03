package com.baleshapp.pocketpaper.view.task.alltasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.databinding.FragmentAllTasksBinding
import com.google.android.material.tabs.TabLayoutMediator

class AllTasksFragment : Fragment() {

    lateinit var binding: FragmentAllTasksBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_tasks, container, false)

        binding.fragment = this

        val activeTaskText = binding.root.context.resources.getString(R.string.active_tasks)
        val completedTaskText = binding.root.context.resources.getString(R.string.completed_tasks)
        val missedTaskText = binding.root.context.resources.getString(R.string.missed_tasks)

        val adapter = FragmentViewPagerAdapter(this)
        binding.allTasksViewPager.adapter = adapter

        TabLayoutMediator(binding.allTaskTabLayout, binding.allTasksViewPager) { tab, position ->
            tab.text = when (position) {
                1 -> completedTaskText
                2 -> missedTaskText
                else -> activeTaskText
            }
        }.attach()

        return binding.root
    }

    fun onBack() {
        val fragmentManager = parentFragmentManager
        fragmentManager.popBackStack()
    }

    class FragmentViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
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