package com.amsavarthan.tally.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.amsavarthan.tally.data.source.local.AccountDao
import com.amsavarthan.tally.domain.entity.Account
import com.amsavarthan.tally.domain.repository.AccountsRepository
import javax.inject.Inject

class DefaultAccountsRepository @Inject constructor(
    private val accountDao: AccountDao,
) : AccountsRepository {
    override fun getAccounts() = accountDao.getAccountEntities()
    override suspend fun getAccount(id: Long) = accountDao.getAccountEntity(id)
    override fun getCashAccount() = accountDao.getCashAccountEntity()

    @Throws(SQLiteConstraintException::class)
    override suspend fun insertAccount(account: Account): Long {
        try {
            return accountDao.insertAccount(account)
        } catch (e: SQLiteConstraintException) {
            throw e
        }
    }

    override suspend fun deleteAccount(account: Account) = accountDao.deleteAccount(account)

    @Throws(SQLiteConstraintException::class)
    override suspend fun updateAccount(account: Account) {
        try {
            accountDao.updateAccount(account)
        } catch (e: SQLiteConstraintException) {
            throw e
        }
    }

    override fun getOutstandingBalanceAmount() = accountDao.getOutstandingBalanceAmount()
    override fun getOutstandingRepaymentAmount() =
        accountDao.getOutstandingRepaymentAmount()
}