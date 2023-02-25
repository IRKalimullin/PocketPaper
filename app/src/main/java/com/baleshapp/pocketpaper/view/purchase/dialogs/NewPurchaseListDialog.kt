package com.baleshapp.pocketpaper.view.purchase.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.PurchaseList
import com.baleshapp.pocketpaper.databinding.PurchaseNewListBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class NewPurchaseListDialog(
    context: Context,
    private val onSave: (list: PurchaseList) -> Unit
) :
    BottomSheetDialog(context) {

    private val emptyNameMessage = context.resources.getString(R.string.empty_name)
    lateinit var dialog: BottomSheetDialog
    val binding: PurchaseNewListBinding
    var list = PurchaseList(
        name = "",
        timestampOfList = System.currentTimeMillis()
    )

    init {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.purchase_new_list, null, false)
        binding.list = list
        binding.dialog = this
        createDialog()
    }

    private fun createDialog() {
        val inputManager: InputMethodManager = context.getSystemService()!!
        inputManager.showSoftInput(binding.root, InputMethodManager.SHOW_IMPLICIT)
        dialog = BottomSheetDialog(context, R.style.app_bottom_sheet_dialog_style)
        dialog.setContentView(binding.root)

        dialog.setOnCancelListener {
            inputManager.showSoftInput(binding.root, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }
        dialog.show()
        binding.categoryAddInputName.requestFocus()
    }

    fun saveList() {
        if (isValidated()) {
            onSave(list)
            dialog.cancel()
        }
    }

    private fun isValidated(): Boolean {
        return if (list.name.isNotEmpty()) {
            true
        } else {
            Toast.makeText(context, emptyNameMessage, Toast.LENGTH_SHORT).show()
            return false
        }
    }
}