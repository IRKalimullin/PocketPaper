package com.baleshapp.pocketpaper.data.database

import androidx.room.*
import com.baleshapp.pocketpaper.data.model.FinanceItem
import com.baleshapp.pocketpaper.data.model.FinancialReport
import kotlinx.coroutines.flow.Flow

@Dao
interface FinanceDao {

    @Query("SELECT sum(value) FROM FinanceItem WHERE type = :transactionType AND currentFinancialReportId = :accountId")
    fun getSumAllFinancesValue(transactionType: Int, accountId: Int): Flow<Int?>?

    @Query("SELECT sum(value) FROM FinanceItem WHERE currentFinancialReportId = :accountId AND type = 2")
    suspend  fun getReceiveTotalValue(accountId: Int): Int

    @Query("SELECT sum(value) FROM FinanceItem WHERE currentFinancialReportId = :accountId AND type = 1")
    suspend fun getSpentTotalValue(accountId: Int): Int

    @Query("SELECT count(id) FROM FinancialReport")
    suspend  fun getFinancialAccountsCount(): Int

    @Query("SELECT id FROM FinancialReport ORDER BY id DESC")
    suspend fun getFinancialAccountsIdArray(): IntArray

    @Query("SELECT * FROM FinancialReport")
    fun getAllFinancialAccountsLiveData(): Flow<List<FinancialReport>>


    @Query("SELECT * FROM FinanceItem WHERE type = :transactionType AND currentFinancialReportId = :idAccount ORDER BY name")
    fun getAllFinancesListLiveData(
        transactionType: Int,
        idAccount: Int
    ): Flow<List<FinanceItem>>

    @Query(
        "SELECT sum(value) FROM FinanceItem WHERE type = :transactionType AND currentFinancialReportId = :accountId GROUP BY name ORDER BY name"
    )
    fun getSumFinancesValue(transactionType: Int, accountId: Int): Flow<IntArray?>?

    @Query("DELETE FROM FinanceItem WHERE currentFinancialReportId = :accountId")
    suspend  fun deleteFinanceValuesFromAccount(accountId: Int)

    @Query("SELECT name FROM FinancialReport WHERE id = :financialAccountId")
    fun getFinancialAccountName(financialAccountId: Int): Flow<String?>?

    @Query("SELECT name FROM FinanceItem WHERE type = :transactionType GROUP BY name ORDER BY name")
    suspend fun getSumFinancesCategoryColor(transactionType: Int): IntArray

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun insert(finances: FinanceItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(financialAccount: FinancialReport)

    @Update
    suspend  fun update(finances: FinanceItem)

    @Update
    suspend  fun updateFinancialAccount(financialAccount: FinancialReport)

    @Delete
    suspend  fun deleteFinancialAccount(financialAccount: FinancialReport)

    @Delete
    suspend fun delete(finances: FinanceItem)
}