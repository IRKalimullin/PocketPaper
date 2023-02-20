package com.baleshapp.pocketpaper.view.purchase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.PurchaseItem
import com.baleshapp.pocketpaper.data.model.PurchaseList
import com.baleshapp.pocketpaper.data.repository.PurchaseRepository
import com.baleshapp.pocketpaper.databinding.ActivityPurchaseListItemBinding
import com.baleshapp.pocketpaper.view.purchase.adapters.PurchaseAdapter
import com.baleshapp.pocketpaper.viewmodel.purchase.PurchaseViewModel
import com.baleshapp.pocketpaper.viewmodel.purchase.PurchaseViewModelFactory

class PurchaseListItemActivity : AppCompatActivity() {

    lateinit var binding: ActivityPurchaseListItemBinding
    lateinit var purchaseList: PurchaseList
    lateinit var viewModel: PurchaseViewModel
    var newItem = PurchaseItem(
        categoryId = 0,
        name = "",
        number = 0,
        isAdded = false,
        timestampOfItem = System.currentTimeMillis()
    )
    var purchaseAdapter:  PurchaseAdapter? = PurchaseAdapter(emptyList(),{viewModel.deleteItem(it)},{viewModel.updateItem(it)})


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        purchaseList = intent.getSerializableExtra(PURCHASE_LIST_EXTRA_KEY) as PurchaseList

        newItem.categoryId = purchaseList.id

        val inflater = LayoutInflater.from(this)

        binding = DataBindingUtil.inflate(inflater,R.layout.activity_purchase_list_item, null,false)

        val viewModelFactory = PurchaseViewModelFactory(
            PurchaseRepository(binding.root.context)
        )
        viewModel = ViewModelProvider(this, viewModelFactory)[PurchaseViewModel::class.java]

       // val purchaseAdapter = PurchaseAdapter({viewModel.deleteItem(it)},{viewModel.updateItem(it)})

        viewModel.getItemList(purchaseList).observe(this){
            purchaseAdapter?.submitList(it)
        }

        binding.activity = this
        binding.purchaseList = purchaseList
        binding.purchaseItem = newItem

        binding.purchaseItemRecyclerView.adapter = purchaseAdapter

        setContentView(binding.root)
    }

    fun saveItem(){
        viewModel.insertItem(newItem)
        binding.purchasesItemsAddLineName.text.clear()
        binding.invalidateAll()
        newItem.timestampOfItem = System.currentTimeMillis()
    }
}