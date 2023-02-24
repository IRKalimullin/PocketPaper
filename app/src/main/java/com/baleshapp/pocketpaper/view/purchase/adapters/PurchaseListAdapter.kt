package com.baleshapp.pocketpaper.view.purchase.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.PurchaseList
import com.baleshapp.pocketpaper.databinding.PurchaseListItemBinding

class PurchaseListAdapter(
    private val onDelete: (list: PurchaseList) -> Unit,
    private val onListSelected: (list: PurchaseList) -> Unit
) : RecyclerView.Adapter<PurchaseListAdapter.PurchaseListViewHolder>() {

    private var list: SortedList<PurchaseList> =
        SortedList(PurchaseList::class.java, object : SortedList.Callback<PurchaseList>() {
            override fun compare(o1: PurchaseList, o2: PurchaseList): Int {
                return (o2.timestampOfList - o1.timestampOfList).toInt()
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

            override fun areContentsTheSame(
                oldItem: PurchaseList?,
                newItem: PurchaseList?
            ): Boolean {
                return oldItem?.equals(newItem)!!
            }

            override fun areItemsTheSame(item1: PurchaseList, item2: PurchaseList): Boolean {
                return item1.id == item2.id
            }

        })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<PurchaseListItemBinding>(
            inflater,
            R.layout.purchase_list_item,
            parent,
            false
        )
        return PurchaseListViewHolder(binding, onDelete, onListSelected)
    }

    override fun onBindViewHolder(holder: PurchaseListViewHolder, position: Int) {
        holder.bind(list.get(position))
    }

    fun setItems(list: List<PurchaseList>) {
        this.list.replaceAll(list)
    }

    override fun getItemCount(): Int = list.size()

    class PurchaseListViewHolder(
        binding: PurchaseListItemBinding,
        private val onDelete: (list: PurchaseList) -> Unit,
        private val onListSelected: (list: PurchaseList) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        lateinit var list: PurchaseList
        private val mBinding: PurchaseListItemBinding = binding

        private val cancelWarningMessage =
            binding.root.resources.getString(R.string.cancel_warning_message)
        private val deleteMessage = binding.root.resources.getString(R.string.delete)
        private val cancelMessage = binding.root.resources.getString(R.string.cancel)
        private val deletedMessage = binding.root.resources.getString(R.string.deleted)


        fun bind(list: PurchaseList) {
            this.list = list
            mBinding.list = this.list
            mBinding.viewHolder = this
            mBinding.executePendingBindings()
        }

        fun onLongClick(): Boolean {
            val builder = AlertDialog.Builder(mBinding.root.context, R.style.app_alert_dialog_style)
            builder.setTitle("$deleteMessage \"${list.name}\"?")
                .setMessage(cancelWarningMessage)
                .setPositiveButton(
                    deleteMessage
                ) { _, _ -> deleteList() }
                .setNegativeButton(
                    cancelMessage
                ) { dialog, _ -> dialog.cancel() }

            val alertDialog = builder.create()
            alertDialog.show()
            return true
        }

        private fun deleteList() {
            onDelete(list)
            Toast.makeText(mBinding.root.context, deletedMessage, Toast.LENGTH_SHORT).show()
        }

        fun openList() {
            onListSelected(list)
        }
    }
}