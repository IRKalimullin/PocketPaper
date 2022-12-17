package com.baleshapp.pocketpaper.view.purchase.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.PurchaseItem
import com.baleshapp.pocketpaper.databinding.PurchaseItemBinding

class PurchaseAdapter(
    private val onDelete: (item: PurchaseItem) -> Unit,
    private val onUpdate: (item: PurchaseItem) -> Unit
) : RecyclerView.Adapter<PurchaseAdapter.PurchaseViewHolder>() {

    private var itemList: SortedList<PurchaseItem> =
        SortedList(PurchaseItem::class.java, object : SortedList.Callback<PurchaseItem>() {
            override fun compare(o1: PurchaseItem, o2: PurchaseItem): Int {
                return if (!o2.isAdded && o1.isAdded) {
                    -1
                } else if (o2.isAdded && !o1.isAdded) {
                    1
                } else {
                    (o2.timestampOfItem - o1.timestampOfItem).toInt()
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

            override fun areContentsTheSame(
                oldItem: PurchaseItem?,
                newItem: PurchaseItem?
            ): Boolean {
                return oldItem?.equals(newItem)!!
            }

            override fun areItemsTheSame(item1: PurchaseItem, item2: PurchaseItem): Boolean {
                return item1.id == item2.id
            }

        })

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
        holder.bind(itemList.get(position))
    }

    fun setItems(itemList: List<PurchaseItem>) {
        this.itemList.replaceAll(itemList)
    }

    override fun getItemCount(): Int = itemList.size()

    class PurchaseViewHolder(
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

        fun saveCheckedState(compoundButton: CompoundButton, isChecked: Boolean) {
            item.isAdded = isChecked
            onUpdate(item)
            mBinding.invalidateAll()
        }

        fun onLongClick(): Boolean {
            val builder = AlertDialog.Builder(mBinding.root.context, R.style.custom_alert_dialog)
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
}
