package com.baleshapp.pocketpaper.view.task.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.Task
import com.baleshapp.pocketpaper.databinding.TaskItemBinding
import com.baleshapp.pocketpaper.utils.DateTimeUtil


class TaskAdapter(
    private val onDelete: (task: Task) -> Unit,
    private val onUpdate: (task: Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var taskList: SortedList<Task> =
        SortedList(Task::class.java, object : SortedList.Callback<Task>() {
            override fun compare(o1: Task, o2: Task): Int {
                return if (!o2.isDone && o1.isDone) {
                    1
                } else if (o2.isDone && !o1.isDone) {
                    -1
                } else {
                    (o2.timestampOfTask - o1.timestampOfTask).toInt()
                }
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

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(item1: Task, item2: Task): Boolean {
                return item1.id == item2.id
            }

        })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<TaskItemBinding>(
            inflater,
            R.layout.task_item,
            parent,
            false
        )
        return TaskViewHolder(binding, onDelete, onUpdate)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position])
    }

    fun setItems(taskList: List<Task>) {
        this.taskList.replaceAll(taskList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = taskList.size()

    class TaskViewHolder(
        binding: TaskItemBinding,
        private val onDelete: (task: Task) -> Unit,
        private val onUpdate: (task: Task) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        lateinit var task: Task
        private val mBinding: TaskItemBinding = binding
        private val dateTimeUtil = DateTimeUtil()
        private val deleteTaskMessage =
            binding.root.context.resources.getString(R.string.delete_task)
        private val deleteMessage = binding.root.context.resources.getString(R.string.delete)
        private val cancelMessage = binding.root.context.resources.getString(R.string.cancel)
        private val cancelWarningMessage =
            binding.root.context.resources.getString(R.string.cancel_warning_message)
        private val deletedMessage = binding.root.context.resources.getString(R.string.deleted)

        init {
            mBinding.viewHolder = this
        }

        fun bind(task: Task) {
            this.task = task
            mBinding.task = this.task
            mBinding.invalidateAll()
        }

        fun getDateString(task: Task): String {
            return dateTimeUtil.getDateString(task.date)
        }

        fun getTimeString(task: Task): String {
            val timeString = dateTimeUtil.getTimeString(task.time)
            return if (timeString == "04:00") {
                ""
            } else {
                timeString
            }
        }

        fun saveCheckedState(compoundButton: CompoundButton, isChecked: Boolean) {
            task.isDone = isChecked
            onUpdate(task)
            mBinding.invalidateAll()
        }

        fun onLongClick(): Boolean {
            val builder = AlertDialog.Builder(mBinding.root.context, R.style.custom_alert_dialog)
            builder.setTitle("$deleteTaskMessage \"${task.name}\"?")
                .setMessage(cancelWarningMessage)
                .setPositiveButton(
                    deleteMessage
                ) { _, _ -> deleteTask() }
                .setNegativeButton(
                    cancelMessage
                ) { dialog, _ -> dialog.cancel() }

            val alertDialog = builder.create()
            alertDialog.show()
            return true
        }

        private fun deleteTask() {
            onDelete(task)
            Toast.makeText(mBinding.root.context, deletedMessage, Toast.LENGTH_SHORT).show()
        }
    }
}
