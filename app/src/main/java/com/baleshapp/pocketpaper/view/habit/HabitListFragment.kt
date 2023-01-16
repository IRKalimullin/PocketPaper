package com.baleshapp.pocketpaper.view.habit

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
import com.baleshapp.pocketpaper.data.model.Habit
import com.baleshapp.pocketpaper.data.repository.HabitRepository
import com.baleshapp.pocketpaper.databinding.FragmentHabitListBinding
import com.baleshapp.pocketpaper.view.habit.adapters.HabitListAdapter
import com.baleshapp.pocketpaper.view.habit.dialog.NewHabitDialog
import com.baleshapp.pocketpaper.viewmodel.habit.HabitViewModel
import com.baleshapp.pocketpaper.viewmodel.habit.HabitViewModelFactory

class HabitListFragment : Fragment() {

    lateinit var binding: FragmentHabitListBinding
    private lateinit var habitViewModel: HabitViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_habit_list, container, false)

        binding.fragment = this

        val viewModelFactory = HabitViewModelFactory(
            HabitRepository(binding.root.context)
        )

        habitViewModel = ViewModelProvider(this, viewModelFactory)[HabitViewModel::class.java]

        val adapter = HabitListAdapter({ habitViewModel.fullDeleteHabit(it) },{openHabitDetail(it)})

        habitViewModel.getHabitList().observe(viewLifecycleOwner) {
            adapter.setItems(it)
        }

        val layoutManager =
            LinearLayoutManager(binding.root.context, RecyclerView.VERTICAL, false)

        binding.habitsRecyclerView.layoutManager = layoutManager

        binding.habitsRecyclerView.adapter = adapter

        return binding.root
    }

    fun createNewHabit() {
        NewHabitDialog(binding.root.context) { habitViewModel.saveHabit(it) }
    }

    private fun openHabitDetail(habit: Habit) {
        val fragmentManager = parentFragmentManager
        fragmentManager.beginTransaction().replace(R.id.master_container, HabitDetailFragment(habit))
            .addToBackStack(null).commit()
    }

    fun onBack() {
        val fragmentManager = parentFragmentManager
        fragmentManager.popBackStack()
    }
}