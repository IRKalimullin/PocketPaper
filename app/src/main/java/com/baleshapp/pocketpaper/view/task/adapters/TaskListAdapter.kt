package com.baleshapp.pocketpaper.view.task.adapters

import android.app.AlertDialog
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.Task
import com.baleshapp.pocketpaper.data.model.TaskTag
import com.baleshapp.pocketpaper.databinding.TaskItemViewBinding
import com.baleshapp.pocketpaper.utils.DateTimeUtil
import com.baleshapp.pocketpaper.view.task.dialogs.TaskDetailDialog

class TaskListAdapter(
    private val onDelete: (task: Task) -> Unit,
    private val onUpdate: (task: Task) -> Unit
) : RecyclerView.Adapter<TaskListAdapter.TaskItemViewHolder>() {
    //NEW CLASS REDESIGN
    private var taskList = listOf<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<TaskItemViewBinding>(
            inflater,
            R.layout.task_item_view,
            parent,
            false
        )
        return TaskItemViewHolder(binding, onDelete, onUpdate)
    }

    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        holder.bind(taskList[position])
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    fun setData(list: List<Task>){
        taskList = list.toMutableList()
    }


    class TaskItemViewHolder(
        binding: TaskItemViewBinding,
        private val onDelete: (task: Task) -> Unit,
        private val onUpdate: (task: Task) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        lateinit var task: Task
        private val mBinding: TaskItemViewBinding = binding
        private val dateTimeUtil = DateTimeUtil()
        private val context = binding.root.context
        var isDescription = false

        var tagText: String = context.resources.getString(R.string.general_tag_text)

        private val deleteTaskMessage =
            context.resources.getString(R.string.delete_task)
        private val deleteMessage = context.resources.getString(R.string.delete)
        private val cancelMessage = context.resources.getString(R.string.cancel)
        private val cancelWarningMessage =
            context.resources.getString(R.string.cancel_warning_message)
        private val deletedMessage = context.resources.getString(R.string.deleted)

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
                TaskTag.GENERAL -> R.color.general_task_tag_color
                TaskTag.PERSONAL -> R.color.personal_task_tag_color
                TaskTag.WORK -> R.color.work_task_tag_color
                TaskTag.STUDY -> R.color.study_task_tag_color
            }
        }

        private fun getTagText(tag: TaskTag): String {
            return when (tag) {
                TaskTag.GENERAL -> context.resources.getString(R.string.general_tag_text)
                TaskTag.PERSONAL -> context.resources.getString(R.string.personal_tag_text)
                TaskTag.WORK -> context.resources.getString(R.string.work_tag_text)
                TaskTag.STUDY -> context.resources.getString(R.string.study_tag_text)
            }
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
            val builder = AlertDialog.Builder(context, R.style.custom_alert_dialog)
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

        fun openTaskDetails(){
            TaskDetailDialog(context, task, onDelete, onUpdate)
        }

        private fun deleteTask() {
            onDelete(task)
            Toast.makeText(context, deletedMessage, Toast.LENGTH_SHORT).show()
        }
    }
}