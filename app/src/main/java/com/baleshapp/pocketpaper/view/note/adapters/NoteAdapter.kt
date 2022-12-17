package com.baleshapp.pocketpaper.view.note.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.Note
import com.baleshapp.pocketpaper.databinding.NoteItemBinding
import com.baleshapp.pocketpaper.view.note.dialogs.NoteDialog


class NoteAdapter(
    private val onDelete: (note: Note) -> Unit,
    private val onUpdate: (note: Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var noteList: SortedList<Note> =
        SortedList(Note::class.java, object : SortedList.Callback<Note>() {
            override fun compare(o1: Note, o2: Note): Int {
                return if (!o2.isFavorite && o1.isFavorite) {
                    -1
                } else if (o2.isFavorite && !o1.isFavorite) {
                    1
                } else {
                    (o2.timestampOfNote - o1.timestampOfNote).toInt()
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

            override fun areContentsTheSame(oldItem: Note?, newItem: Note?): Boolean {
                return oldItem?.equals(newItem)!!
            }

            override fun areItemsTheSame(item1: Note, item2: Note): Boolean {
                return item1.id == item2.id
            }

        })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<NoteItemBinding>(
            inflater,
            R.layout.note_item,
            parent,
            false
        )
        return NoteViewHolder(binding, onDelete, onUpdate)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(noteList.get(position))
    }

    fun setItems(noteList: List<Note>) {
        this.noteList.replaceAll(noteList)
    }

    override fun getItemCount(): Int = noteList.size()

    class NoteViewHolder(
        binding: NoteItemBinding,
        private val onDelete: (note: Note) -> Unit,
        private val onUpdate: (note: Note) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        lateinit var note: Note
        private val mBinding: NoteItemBinding = binding
        private val cancelWarningMessage =
            binding.root.resources.getString(R.string.cancel_warning_message)
        private val deleteMessage = binding.root.resources.getString(R.string.delete)
        private val cancelMessage = binding.root.resources.getString(R.string.cancel)
        private val deleteNoteMessage = binding.root.resources.getString(R.string.delete_note)
        private val deletedMessage = binding.root.resources.getString(R.string.deleted)

        fun bind(note: Note) {
            this.note = note
            mBinding.note = this.note
            mBinding.viewHolder = this
            mBinding.executePendingBindings()
        }

        fun saveCheckedState(compoundButton: CompoundButton, isChecked: Boolean) {
            note.isFavorite = isChecked
            onUpdate(note)
            mBinding.invalidateAll()
        }

        fun createNoteDialog() {
            NoteDialog(
                mBinding.root.context,
                false, note,
                onDelete,
                onUpdate
            )
        }

        fun onLongClick(): Boolean {
            val builder = AlertDialog.Builder(mBinding.root.context, R.style.custom_alert_dialog)
            builder.setTitle("$deleteNoteMessage \"${note.name}\"?")
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
            onDelete(note)
            Toast.makeText(mBinding.root.context, deletedMessage, Toast.LENGTH_SHORT).show()
        }
    }
}
