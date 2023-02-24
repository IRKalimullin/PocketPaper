package com.baleshapp.pocketpaper.view.habit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.Habit
import com.baleshapp.pocketpaper.data.repository.HabitRepository
import com.baleshapp.pocketpaper.databinding.ActivityHabitListBinding
import com.baleshapp.pocketpaper.view.habit.adapters.HabitListAdapter
import com.baleshapp.pocketpaper.viewmodel.habit.HabitViewModel
import com.baleshapp.pocketpaper.viewmodel.habit.HabitViewModelFactory

const val HABIT_EXTRA_KEY = "HABIT_EXTRA_KEY"

class HabitListActivity : AppCompatActivity() {

    var isListEmpty = true
    private lateinit var habitViewModel: HabitViewModel
    private lateinit var binding: ActivityHabitListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_habit_list)

        val viewModelFactory = HabitViewModelFactory(
            HabitRepository(binding.root.context)
        )
        habitViewModel = ViewModelProvider(this, viewModelFactory)[HabitViewModel::class.java]

        val adapter =
            HabitListAdapter({ habitViewModel.fullDeleteHabit(it) }, { openHabitDetail(it) })

        habitViewModel.getHabitList().observe(this) {
            adapter.setItems(it)
            changeVisibility(it.isEmpty())
        }

        binding.activity = this
        binding.habitListRecyclerView.adapter = adapter
        setContentView(binding.root)
    }

    fun createNewHabit() {
        openHabitDetail(null)
    }

    private fun changeVisibility(flag: Boolean) {
        isListEmpty = flag
        binding.invalidateAll()
    }

    private fun openHabitDetail(habit: Habit?) {
        val intent = Intent(this, HabitDetailActivity::class.java).apply {
            putExtra(HABIT_EXTRA_KEY, habit)
        }
        startActivity(intent)
    }
}