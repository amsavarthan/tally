package com.amsavarthan.tally.domain.repository

import com.amsavarthan.tally.domain.entity.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    suspend fun insertTransaction(transaction: Transaction)
    suspend fun deleteTransaction(transaction: Transaction)
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun getTransaction(id: Long): Transaction

    fun getTransactions(): Flow<List<Transaction>>
    fun getCurrentWeekTransactions(): Flow<List<Transaction>>

    fun getAmountSpentInCurrentWeek(): Flow<Double>

}