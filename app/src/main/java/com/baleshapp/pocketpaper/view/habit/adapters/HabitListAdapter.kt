package com.baleshapp.pocketpaper.view.habit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.Habit
import com.baleshapp.pocketpaper.databinding.HabitItemBinding
import com.baleshapp.pocketpaper.view.habit.dialog.DeleteHabitDialog

class HabitListAdapter(
    private val onDelete: (habit: Habit) -> Unit,
    private val openHabitDetail: (habit: Habit) -> Unit
) : RecyclerView.Adapter<HabitListAdapter.HabitListItemViewHolder>() {

    private var habitList: SortedList<Habit> =
        SortedList(Habit::class.java, object : SortedList.Callback<Habit>() {
            override fun compare(o1: Habit, o2: Habit): Int {
                return o2.id - o1.id
            }

            override fun onInserted(position: Int, count: Int) {
                notifyItemInserted(position)
            }

            override fun onRemoved(position: Int, count: Int) {
                notifyItemRemoved(position)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                notifyItemMoved(fromPosition, toPosition)
            }

            override fun onChanged(position: Int, count: Int) {
                notifyItemRangeChanged(position, count)
            }

            override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(item1: Habit, item2: Habit): Boolean {
                return item1.id == item2.id
            }

        })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitListItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<HabitItemBinding>(
            inflater,
            R.layout.habit_item,
            parent,
            false
        )
        return HabitListItemViewHolder(binding, onDelete, openHabitDetail)
    }

    override fun onBindViewHolder(holder: HabitListItemViewHolder, position: Int) {
        holder.bind(habitList.get(position))
    }

    fun setItems(habitList: List<Habit>) {
        this.habitList.replaceAll(habitList)
    }

    override fun getItemCount(): Int = habitList.size()

    class HabitListItemViewHolder(
        private val binding: HabitItemBinding,
        private val onDelete: (habit: Habit) -> Unit,
        private val openHabitDetail: (habit: Habit) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        lateinit var habit: Habit

        fun bind(habit: Habit) {
            this.habit = habit
            binding.habit = this.habit
            binding.viewHolder = this
            binding.executePendingBindings()
        }

        fun onLongClick(): Boolean {
            DeleteHabitDialog(binding.root.context,habit,onDelete)
            return true
        }

        fun openHabitDetail() {
            openHabitDetail(habit)
        }
    }
}