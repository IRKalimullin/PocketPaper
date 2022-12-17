package com.baleshapp.pocketpaper.data.database

import androidx.room.*
import com.baleshapp.pocketpaper.data.model.PurchaseItem
import com.baleshapp.pocketpaper.data.model.PurchaseList
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchaseDao {

    @Query("SELECT * FROM PurchaseItem where categoryId = :categoryId")
    fun getItemList(categoryId: Int): Flow<List<PurchaseItem>>

    @Query("SELECT * FROM PurchaseList")
    fun getAllLists(): Flow<List<PurchaseList>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(purchaseItem: PurchaseItem)

    @Query("DELETE FROM PurchaseItem WHERE categoryId = :categoryId")
    suspend fun deleteItemsAfterList(categoryId: Int)

    @Update
    suspend fun updateItem(purchaseItem: PurchaseItem)

    @Delete
    suspend fun deleteItem(purchaseItem: PurchaseItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(purchaseList: PurchaseList)

    @Update
    suspend fun update(purchaseList: PurchaseList)

    @Delete
    suspend fun delete(purchaseList: PurchaseList)
}