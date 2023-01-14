package com.amsavarthan.tally.data.source.local

import androidx.room.*
import com.amsavarthan.tally.domain.entity.Account
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Insert
    suspend fun insertAccount(account: Account): Long

    @Delete
    suspend fun deleteAccount(account: Account)

    @Update
    suspend fun updateAccount(account: Account)

    @Query("SELECT * FROM account ORDER BY name COLLATE NOCASE ASC")
    fun getAccountEntities(): Flow<List<Account>>

    @Query("SELECT * FROM account WHERE id = :id")
    suspend fun getAccountEntity(id: Long): Account

    @Query("SELECT * FROM account WHERE type == 'cash'")
    suspend fun getCashAccountEntity(): Account?

    @Query(
        """
        SELECT SUM(COALESCE(balance,0))
        FROM account
        WHERE type IN ("cash","debit card")
    """
    )
    fun getOutstandingBalanceAmount(): Flow<Double>

    @Query(
        """
        SELECT SUM(COALESCE(balance,0))
        FROM account
        WHERE type IN ("credit card","pay later")
    """
    )
    fun getOutstandingRepaymentAmount(): Flow<Double>

}