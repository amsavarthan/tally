package com.amsavarthan.tally.data.source.local

import androidx.room.*
import com.amsavarthan.tally.domain.entity.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert
    suspend fun insertEntry(transaction: Transaction)

    @Delete
    suspend fun deleteEntry(transaction: Transaction)

    @Query("SELECT * FROM entry ORDER BY datetime(date_time) DESC")
    fun getTransactionEntries(): Flow<List<Transaction>>

    @Query("SELECT * FROM entry WHERE id = :id")
    suspend fun getTransactionEntry(id: Long): Transaction

    @Query("SELECT MAX(COALESCE(SUM(CASE WHEN transaction_type = 'expense' THEN amount ELSE -amount END), 0),0) FROM entry WHERE strftime('%W',date_time,'utc') = strftime('%W','now','utc')")
    fun getAmountSpentInCurrentWeek(): Flow<Double>

    @Query("SELECT * FROM entry WHERE strftime('%W',date_time,'utc') = strftime('%W','now','utc') ORDER BY datetime(date_time) DESC")
    fun getEntriesOfCurrentWeek(): Flow<List<Transaction>>

}