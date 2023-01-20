package com.baleshapp.pocketpaper.view.habit.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.Habit
import com.baleshapp.pocketpaper.databinding.HabitItemBinding

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

        private val context = binding.root.context
        lateinit var habit: Habit
        private val cancelWarningMessage =
            binding.root.resources.getString(R.string.cancel_warning_message)
        private val deleteMessage = binding.root.resources.getString(R.string.delete)
        private val cancelMessage = binding.root.resources.getString(R.string.cancel)

        private val deleteHabitMessage = context.resources.getString(R.string.delete_habit)
        private val deletedMessage = binding.root.resources.getString(R.string.deleted)

        fun bind(habit: Habit) {
            this.habit = habit
            binding.habit = this.habit
            binding.viewHolder = this
            binding.executePendingBindings()
        }

        fun onLongClick(): Boolean {
            val builder = AlertDialog.Builder(context, R.style.custom_alert_dialog)
            builder.setTitle("$deleteHabitMessage \"${habit.name}\"?")
                .setMessage(cancelWarningMessage)
                .setPositiveButton(
                    deleteMessage
                ) { _, _ -> deleteHabit() }
                .setNegativeButton(
                    cancelMessage
                ) { dialog, _ -> dialog.cancel() }

            val alertDialog = builder.create()
            alertDialog.show()
            return true
        }

        fun openHabitDetail() {
            openHabitDetail(habit)
        }

        private fun deleteHabit() {
            onDelete(habit)
            Toast.makeText(context, deletedMessage, Toast.LENGTH_SHORT).show()
        }
    }
}