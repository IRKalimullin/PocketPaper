package com.baleshapp.pocketpaper.data.repository

import android.content.Context
import com.baleshapp.pocketpaper.data.database.AppDatabase
import com.baleshapp.pocketpaper.data.database.PurchaseDao
import com.baleshapp.pocketpaper.data.model.PurchaseItem
import com.baleshapp.pocketpaper.data.model.PurchaseList
import kotlinx.coroutines.flow.Flow

class PurchaseRepository(context: Context) {

    private val purchaseDao: PurchaseDao

    init {
        val database = AppDatabase.getDatabase(context)
        purchaseDao = database!!.purchaseDao()
    }

    fun getItemsList(purchaseList: PurchaseList): Flow<List<PurchaseItem>> {
        return purchaseDao.getItemList(purchaseList.id)
    }

    fun getPurchaseList(): Flow<List<PurchaseList>> {
        return purchaseDao.getAllLists()
    }

    suspend fun updateItem(purchaseItem: PurchaseItem) {
        purchaseDao.updateItem(purchaseItem)
    }

    suspend fun insertItem(purchaseItem: PurchaseItem) {
        purchaseDao.insertItem(purchaseItem)
    }

    suspend fun deleteItem(purchaseItem: PurchaseItem) {
        purchaseDao.deleteItem(purchaseItem)
    }

    suspend fun insertList(purchaseList: PurchaseList) {
        purchaseDao.insert(purchaseList)
    }

    suspend fun deleteList(purchaseList: PurchaseList) {
        purchaseDao.delete(purchaseList)
        purchaseDao.deleteItemsAfterList(purchaseList.id)
    }
}