package com.baleshapp.pocketpaper.view.purchase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baleshapp.pocketpaper.R
import com.baleshapp.pocketpaper.data.model.PurchaseList
import com.baleshapp.pocketpaper.data.repository.PurchaseRepository
import com.baleshapp.pocketpaper.databinding.FragmentPurchaseBinding
import com.baleshapp.pocketpaper.view.purchase.adapters.PurchaseListAdapter
import com.baleshapp.pocketpaper.view.purchase.dialogs.NewPurchaseListDialog
import com.baleshapp.pocketpaper.view.purchase.dialogs.PurchaseListDialog
import com.baleshapp.pocketpaper.viewmodel.purchase.PurchaseViewModel
import com.baleshapp.pocketpaper.viewmodel.purchase.PurchaseViewModelFactory

class PurchaseFragment : Fragment() {

    lateinit var binding: FragmentPurchaseBinding
    lateinit var viewModel: PurchaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_purchase, container, false)
        binding.fragment = this

        val viewModelFactory = PurchaseViewModelFactory(
            PurchaseRepository(binding.root.context)
        )

        viewModel = ViewModelProvider(this, viewModelFactory)[PurchaseViewModel::class.java]
        val listAdapter = PurchaseListAdapter({ viewModel.deleteList(it) }, { openList(it) })

        viewModel.getAllLists().observe(viewLifecycleOwner) {
            listAdapter.setItems(it)
        }

        val layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.VERTICAL, false)
        binding.purchaseListRecyclerView.layoutManager = layoutManager
        binding.purchaseListRecyclerView.adapter = listAdapter
        return binding.root
    }

    fun createNewList() {
        NewPurchaseListDialog(binding.root.context) { viewModel.insertList(it) }
    }

    private fun openList(list: PurchaseList) {
        val dialog = PurchaseListDialog(
            binding.root.context,
            list,
            { viewModel.insertItem(it) },
            { viewModel.deleteItem(it) },
            { viewModel.updateItem(it) })
        viewModel.getItemList(list).observe(viewLifecycleOwner) {
            dialog.setData(it)
        }
    }

    fun onBack() {
        val fragmentManager = parentFragmentManager
        fragmentManager.popBackStack()
    }
}