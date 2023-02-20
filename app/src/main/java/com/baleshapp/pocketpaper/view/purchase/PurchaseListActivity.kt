package com.baleshapp.pocketpaper.view.purchase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.Note
import com.baleshapp.pocketpaper.data.model.PurchaseList
import com.baleshapp.pocketpaper.data.repository.PurchaseRepository
import com.baleshapp.pocketpaper.databinding.ActivityPurchaseListBinding
import com.baleshapp.pocketpaper.view.note.NOTE_EXTRA_KEY
import com.baleshapp.pocketpaper.view.note.NoteActivity
import com.baleshapp.pocketpaper.view.purchase.adapters.PurchaseListAdapter
import com.baleshapp.pocketpaper.view.purchase.dialogs.NewPurchaseListDialog
import com.baleshapp.pocketpaper.viewmodel.purchase.PurchaseViewModel
import com.baleshapp.pocketpaper.viewmodel.purchase.PurchaseViewModelFactory

const val PURCHASE_LIST_EXTRA_KEY = "PURCHASE_LIST_EXTRA_KEY"

class PurchaseListActivity : AppCompatActivity() {

    var isListEmpty = true
    lateinit var binding: ActivityPurchaseListBinding
    lateinit var viewModel: PurchaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = LayoutInflater.from(this)

        binding = DataBindingUtil.inflate(inflater, R.layout.activity_purchase_list, null, false)

        val viewModelFactory = PurchaseViewModelFactory(
            PurchaseRepository(binding.root.context)
        )
        viewModel = ViewModelProvider(this, viewModelFactory)[PurchaseViewModel::class.java]

        val listAdapter = PurchaseListAdapter({ viewModel.deleteList(it) }, { openList(it) })

        viewModel.getAllLists().observe(this) {
            listAdapter.setItems(it)
            changeVisible(it.isEmpty())
        }

        binding.activity = this
        binding.purchaseListRecyclerView.adapter = listAdapter
        setContentView(binding.root)
    }

    private fun changeVisible(flag: Boolean) {
        isListEmpty = flag
        binding.invalidateAll()
    }

    fun createNewList() {
        NewPurchaseListDialog(this) { viewModel.insertList(it) }
    }

    private fun openList(purchaseList: PurchaseList) {
        val intent = Intent(this, PurchaseListItemActivity::class.java)
        intent.putExtra(PURCHASE_LIST_EXTRA_KEY, purchaseList)
        startActivity(intent)
    }
}