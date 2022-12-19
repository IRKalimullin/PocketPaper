package com.baleshapp.pocketpaper.view.purchase.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.PurchaseItem
import com.baleshapp.pocketpaper.data.model.PurchaseList
import com.baleshapp.pocketpaper.databinding.PurchaseListDialogBinding
import com.baleshapp.pocketpaper.view.purchase.adapters.PurchaseAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog

class PurchaseListDialog(
    context: Context,
    list: PurchaseList,
    private val onSave: (item: PurchaseItem) -> Unit,
    private val onDelete: (item: PurchaseItem) -> Unit,
    private val onUpdate: (item: PurchaseItem) -> Unit
) :
    BottomSheetDialog(context) {

    private val emptyNameMessage = context.resources.getString(R.string.empty_name)
    var numberText = "1"
    var item = PurchaseItem(
        categoryId = list.id,
        name = "",
        number = 1,
        isAdded = false,
        timestampOfItem = System.currentTimeMillis()
    )

    lateinit var dialog: BottomSheetDialog

    val binding: PurchaseListDialogBinding
    private lateinit var adapter: PurchaseAdapter

    init {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.purchase_list_dialog, null, false)
        binding.list = list
        binding.item = item
        binding.dialog = this
        createDialog()
    }

    private fun createDialog() {
        dialog = BottomSheetDialog(context, R.style.bottom_sheet_dialog_style)
        dialog.setContentView(binding.root)

        adapter = PurchaseAdapter(onDelete, onUpdate)

        val layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.VERTICAL, false)
        binding.purchaseRecyclerView.layoutManager = layoutManager
        binding.purchaseRecyclerView.adapter = adapter
        dialog.show()
    }

    fun setData(itemList: List<PurchaseItem>) {
        adapter.setItems(itemList)
        adapter.notifyDataSetChanged()
    }

    fun saveItem() {
        if (isValidated()) {
            onSave(item)
        }
    }

    private fun isValidated(): Boolean {
        return if (item.name.isNotEmpty()) {
            true
        } else {
            Toast.makeText(context, emptyNameMessage, Toast.LENGTH_SHORT).show()
            return false
        }
    }
}