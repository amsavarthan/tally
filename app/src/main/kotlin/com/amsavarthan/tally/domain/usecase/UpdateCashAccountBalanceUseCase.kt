package com.amsavarthan.tally.domain.usecase

import com.amsavarthan.tally.domain.entity.AccountType
import com.amsavarthan.tally.domain.repository.AccountsRepository
import javax.inject.Inject

class UpdateCashAccountBalanceUseCase @Inject constructor(
    private val accountsRepository: AccountsRepository,
) {

    suspend operator fun invoke(accountId: Long?, balance: Double) {
        if (accountId == null) return
        val cashAccount = accountsRepository.getAccount(accountId)

        if (cashAccount.type != AccountType.Cash) return
        accountsRepository.updateAccount(cashAccount.copy(balance = balance))
    }

}