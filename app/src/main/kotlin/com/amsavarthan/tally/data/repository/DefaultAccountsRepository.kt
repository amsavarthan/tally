package com.amsavarthan.tally.data.repository

import com.amsavarthan.tally.data.source.local.AccountDao
import com.amsavarthan.tally.domain.entity.Account
import com.amsavarthan.tally.domain.repository.AccountsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultAccountsRepository @Inject constructor(
    private val accountDao: AccountDao,
) : AccountsRepository {
    override fun getAccounts() = accountDao.getAccountEntities()
    override suspend fun getAccount(id: Long) = accountDao.getAccountEntity(id)
    override fun getCashAccount() = accountDao.getCashAccountEntity()
    override suspend fun insertAccount(account: Account) = accountDao.insertAccount(account)
    override suspend fun deleteAccount(account: Account) = accountDao.deleteAccount(account)
    override suspend fun updateAccount(account: Account) = accountDao.updateAccount(account)
    override fun getOutstandingBalanceAmount() = accountDao.getOutstandingBalanceAmount()
    override fun getOutstandingRepaymentAmount() =
        accountDao.getOutstandingRepaymentAmount()
}