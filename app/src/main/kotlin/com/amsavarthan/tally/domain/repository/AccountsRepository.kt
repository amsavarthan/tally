package com.amsavarthan.tally.domain.repository

import com.amsavarthan.tally.domain.entity.Account
import kotlinx.coroutines.flow.Flow

interface AccountsRepository {

    fun getAccounts(): Flow<List<Account>>

    suspend fun getAccount(id: Long): Account

    suspend fun getCashAccount(): Account?

    suspend fun insertAccount(account: Account): Long

    suspend fun deleteAccount(account: Account)

    suspend fun updateAccount(account: Account)

    fun getOutstandingBalanceAmount(): Flow<Double>

    fun getOutstandingRepaymentAmount(): Flow<Double>

}