package com.amsavarthan.tally.data.source.local

import androidx.room.*
import com.amsavarthan.tally.domain.entity.Account
import com.amsavarthan.tally.domain.entity.AccountType
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
    fun getCashAccountEntity(): Flow<Account?>

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
        WHERE type IN ("credit card","pay later account")
    """
    )
    fun getOutstandingRepaymentAmount(): Flow<Double>

}