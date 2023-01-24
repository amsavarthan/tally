package com.amsavarthan.tally.data.repository

import com.amsavarthan.tally.data.source.local.AccountDao
import com.amsavarthan.tally.data.source.local.TransactionDao
import com.amsavarthan.tally.domain.entity.AccountType
import com.amsavarthan.tally.domain.entity.CategoryType
import com.amsavarthan.tally.domain.entity.Transaction
import com.amsavarthan.tally.domain.repository.TransactionRepository
import javax.inject.Inject

class DefaultTransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao,
) : TransactionRepository {

    override suspend fun insertTransaction(transaction: Transaction) {
        val accountFromDb = accountDao.getAccountEntity(transaction.accountId)
        when (transaction.transactionType) {
            CategoryType.Expense -> {
                val balanceUpdatedAccount = when (accountFromDb.type) {
                    AccountType.Cash, AccountType.DebitCard -> accountFromDb.copy(
                        balance = accountFromDb.balance.minus(transaction.amount)
                    )
                    //Increase repayment balance
                    AccountType.CreditCard, AccountType.PayLater -> accountFromDb.copy(
                        balance = accountFromDb.balance.plus(transaction.amount)
                    )
                }
                if (!balanceUpdatedAccount.isValid()) {
                    when (accountFromDb.type) {
                        AccountType.Cash, AccountType.DebitCard -> throw IllegalStateException(
                            "Insufficient balance in ${accountFromDb.type.title} to record transaction"
                        )
                        AccountType.CreditCard, AccountType.PayLater -> throw IllegalStateException(
                            "Transaction using ${accountFromDb.type.title} exceeds credit limit"
                        )
                    }
                }
                accountDao.updateAccount(balanceUpdatedAccount)
            }
            CategoryType.Income -> {
                val balanceUpdatedAccount = when (accountFromDb.type) {
                    AccountType.Cash, AccountType.DebitCard -> accountFromDb.copy(
                        balance = accountFromDb.balance.plus(transaction.amount)
                    )
                    AccountType.CreditCard, AccountType.PayLater -> accountFromDb.copy(
                        balance = accountFromDb.balance.minus(transaction.amount)
                    )
                }
                if (!balanceUpdatedAccount.isValid() && (accountFromDb.type == AccountType.CreditCard || accountFromDb.type == AccountType.PayLater)) {
                    throw IllegalStateException("Repayment for ${accountFromDb.type.title} exceeds required amount")
                }
                accountDao.updateAccount(balanceUpdatedAccount)
            }
        }
        transactionDao.insertEntry(transaction)
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        val accountFromDb = accountDao.getAccountEntity(transaction.accountId)
        val balanceUpdatedAccount = when (transaction.transactionType) {
            CategoryType.Expense -> {
                when (accountFromDb.type) {
                    AccountType.Cash, AccountType.DebitCard -> accountFromDb.copy(
                        balance = accountFromDb.balance.plus(transaction.amount)
                    )
                    AccountType.CreditCard, AccountType.PayLater -> accountFromDb.copy(
                        balance = accountFromDb.balance.minus(transaction.amount).coerceAtLeast(0.0)
                    )
                }
            }
            CategoryType.Income -> {
                when (accountFromDb.type) {
                    AccountType.Cash, AccountType.DebitCard -> accountFromDb.copy(
                        balance = accountFromDb.balance.minus(transaction.amount).coerceAtLeast(0.0)
                    )
                    AccountType.CreditCard, AccountType.PayLater -> accountFromDb.copy(
                        balance = accountFromDb.balance.plus(transaction.amount)
                            .coerceAtMost(accountFromDb.limit)
                    )
                }
            }
        }
        accountDao.updateAccount(balanceUpdatedAccount)
        transactionDao.deleteEntry(transaction)
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        val existingTransaction = transactionDao.getTransactionEntry(transaction.id!!)
        try {
            deleteTransaction(existingTransaction)
            insertTransaction(transaction)
        } catch (e: IllegalStateException) {
            insertTransaction(existingTransaction)
            throw e
        }
    }

    override suspend fun getTransaction(id: Long) =
        transactionDao.getTransactionEntry(id)

    override fun getTransactions() = transactionDao.getTransactionEntries()

    override fun getAmountSpentInCurrentWeek() = transactionDao.getAmountSpentInCurrentWeek()

    override fun getCurrentWeekTransactions() = transactionDao.getEntriesOfCurrentWeek()

}