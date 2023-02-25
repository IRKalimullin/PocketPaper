package com.baleshapp.pocketpaper.view.purchase.adapters

import android.app.AlertDialog
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.PurchaseItem
import com.baleshapp.pocketpaper.databinding.PurchaseItemBinding

class PurchaseAdapter(
    var list: List<PurchaseItem>,
    private val onDelete: (item: PurchaseItem) -> Unit,
    private val onUpdate: (item: PurchaseItem) -> Unit
) : ListAdapter<PurchaseItem, PurchaseAdapter.PurchaseViewHolder>(PurchaseItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<PurchaseItemBinding>(
            inflater,
            R.layout.purchase_item,
            parent,
            false
        )
        return PurchaseViewHolder(binding, onDelete, onUpdate)
    }

    override fun onBindViewHolder(holder: PurchaseViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class PurchaseViewHolder(
        binding: PurchaseItemBinding,
        private val onDelete: (item: PurchaseItem) -> Unit,
        private val onUpdate: (item: PurchaseItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        lateinit var item: PurchaseItem
        private val mBinding: PurchaseItemBinding = binding
        private val cancelWarningMessage =
            binding.root.resources.getString(R.string.cancel_warning_message)
        private val deleteMessage = binding.root.resources.getString(R.string.delete)
        private val cancelMessage = binding.root.resources.getString(R.string.cancel)
        private val deletedMessage = binding.root.resources.getString(R.string.deleted)

        fun bind(item: PurchaseItem) {
            this.item = item
            mBinding.item = this.item
            mBinding.viewHolder = this
            mBinding.executePendingBindings()
        }

        fun saveCheckedState(isChecked: Boolean) {
            item.isAdded = isChecked
            onUpdate(item)
            if (isChecked) {
                mBinding.purchaseItemName.paintFlags =
                    mBinding.purchaseItemName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                mBinding.purchaseItemName.paintFlags =
                    mBinding.purchaseItemName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

        fun onLongClick(): Boolean {
            val builder = AlertDialog.Builder(mBinding.root.context, R.style.app_alert_dialog_style)
            builder.setTitle("$deleteMessage \"${item.name}\"?")
                .setMessage(cancelWarningMessage)
                .setPositiveButton(
                    deleteMessage
                ) { _, _ -> deleteItem() }
                .setNegativeButton(
                    cancelMessage
                ) { dialog, _ -> dialog.cancel() }

            val alertDialog = builder.create()
            alertDialog.show()
            return true
        }

        private fun deleteItem() {
            onDelete(item)
            Toast.makeText(mBinding.root.context, deletedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    class PurchaseItemDiffCallback : DiffUtil.ItemCallback<PurchaseItem>() {
        override fun areItemsTheSame(oldItem: PurchaseItem, newItem: PurchaseItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PurchaseItem, newItem: PurchaseItem): Boolean {
            return oldItem.id == newItem.id
        }
    }
}
