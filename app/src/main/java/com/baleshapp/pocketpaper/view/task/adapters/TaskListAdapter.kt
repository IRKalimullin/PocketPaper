package com.baleshapp.pocketpaper.view.task.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.Task
import com.baleshapp.pocketpaper.data.model.TaskTag
import com.baleshapp.pocketpaper.databinding.TaskItemViewBinding
import com.baleshapp.pocketpaper.utils.DateTimeUtil
import com.baleshapp.pocketpaper.view.task.dialogs.DeleteTaskDialog

class TaskListAdapter(
    list: List<Task>,
    private val onOpenDetail: (task: Task) -> Unit,
    private val onDelete: (task: Task) -> Unit,
    private val onUpdate: (task: Task) -> Unit
) : ListAdapter<Task, TaskListAdapter.TaskItemViewHolder>(TaskListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<TaskItemViewBinding>(
            inflater,
            R.layout.task_item_view,
            parent,
            false
        )
        return TaskItemViewHolder(binding,onOpenDetail, onDelete, onUpdate)
    }

    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }

    class TaskItemViewHolder(
        binding: TaskItemViewBinding,
        private val onOpenDetail: (task: Task) -> Unit,
        private val onDelete: (task: Task) -> Unit,
        private val onUpdate: (task: Task) -> Unit
    ) : ViewHolder(binding.root) {

        lateinit var task: Task
        private val mBinding: TaskItemViewBinding = binding
        private val dateTimeUtil = DateTimeUtil()
        private val context = binding.root.context
        var isDescription = false
        var tagText: String = context.resources.getString(R.string.general_tag_text)

        init {
            mBinding.viewHolder = this
        }

        fun bind(task: Task) {
            this.task = task
            mBinding.taskTagItemView.setChipBackgroundColorResource(getTagColor(task.tag))
            setTextStyle(task.isDone)
            tagText = getTagText(task.tag)
            mBinding.task = this.task
            isDescription = task.description.isNotEmpty()
            mBinding.invalidateAll()
        }

        private fun getTagColor(tag: TaskTag): Int {
            return when (tag) {
                TaskTag.GENERAL -> R.color.wild_willow
                TaskTag.PERSONAL -> R.color.macaroni_and_cheese
                TaskTag.WORK -> R.color.malibu
                TaskTag.STUDY -> R.color.golden_tainoi
            }
        }

        fun openTaskDetail(){
            onOpenDetail(task)
        }

        private fun getTagText(tag: TaskTag): String {
            return when (tag) {
                TaskTag.GENERAL -> context.resources.getString(R.string.general_tag_text)
                TaskTag.PERSONAL -> context.resources.getString(R.string.personal_tag_text)
                TaskTag.WORK -> context.resources.getString(R.string.work_tag_text)
                TaskTag.STUDY -> context.resources.getString(R.string.study_tag_text)
            }
        }

        fun getTimeString(task: Task): String {
            val timeString = dateTimeUtil.getTimeString(task.time)
            return if (timeString == "04:00") {
                ""
            } else {
                timeString
            }
        }

        fun saveCheckedState(isChecked: Boolean) {
            task.isDone = isChecked
            setTextStyle(isChecked)
            onUpdate(task)
            mBinding.invalidateAll()
        }

        private fun setTextStyle(isChecked: Boolean) {
            if (isChecked) {
                mBinding.taskNameView.paintFlags =
                    mBinding.taskNameView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                mBinding.taskNameView.paintFlags =
                    mBinding.taskNameView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

        fun onLongClick(): Boolean {
            DeleteTaskDialog(mBinding.root.context,task,onDelete)
            return true
        }
    }

    class TaskListDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }
    }
}