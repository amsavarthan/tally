package com.amsavarthan.tally.data.repository

import com.amsavarthan.tally.data.source.local.TransactionDao
import com.amsavarthan.tally.domain.entity.Transaction
import com.amsavarthan.tally.domain.repository.TransactionRepository
import javax.inject.Inject

class DefaultTransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao,
) : TransactionRepository {

    override suspend fun insertTransaction(transaction: Transaction) =
        transactionDao.insertEntry(transaction)

    override suspend fun deleteTransaction(transaction: Transaction) =
        transactionDao.deleteEntry(transaction)

    override suspend fun updateTransaction(transaction: Transaction) =
        transactionDao.updateEntry(transaction)

    override suspend fun getTransaction(id: Long) =
        transactionDao.getTransactionEntry(id)

    override fun getTransactions() = transactionDao.getTransactionEntries()

    override fun getAmountSpentInCurrentWeek() = transactionDao.getAmountSpentInCurrentWeek()

    override fun getCurrentWeekTransactions() = transactionDao.getEntriesOfCurrentWeek()

}