package com.baleshapp.pocketpaper.viewmodel.purchase

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.baleshapp.pocketpaper.data.model.PurchaseItem
import com.baleshapp.pocketpaper.data.model.PurchaseList
import com.baleshapp.pocketpaper.data.repository.PurchaseRepository
import kotlinx.coroutines.launch

class PurchaseViewModel(private val repository: PurchaseRepository) : ViewModel() {

    fun getItemList(purchaseList: PurchaseList): LiveData<List<PurchaseItem>> {
        return repository.getItemsList(purchaseList).asLiveData()
    }

    fun getAllLists(): LiveData<List<PurchaseList>> {
        return repository.getPurchaseList().asLiveData()
    }

    fun insertItem(item: PurchaseItem) = viewModelScope.launch {
        repository.insertItem(item)
    }

    fun insertList(list: PurchaseList) = viewModelScope.launch {
        repository.insertList(list)
    }

    fun deleteItem(item: PurchaseItem) = viewModelScope.launch {
        repository.deleteItem(item)
    }

    fun deleteList(list: PurchaseList) = viewModelScope.launch {
        repository.deleteList(list)
    }

    fun updateItem(item: PurchaseItem) = viewModelScope.launch {
        repository.updateItem(item)
    }
}