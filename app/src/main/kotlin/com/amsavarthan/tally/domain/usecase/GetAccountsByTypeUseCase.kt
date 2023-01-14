package com.amsavarthan.tally.domain.usecase

import com.amsavarthan.tally.domain.entity.Account
import com.amsavarthan.tally.domain.entity.AccountType
import com.amsavarthan.tally.domain.repository.AccountsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAccountsByTypeUseCase @Inject constructor(
    private val accountsRepository: AccountsRepository,
) {

    operator fun invoke(accountType: AccountType): Flow<List<Account>> {
        return accountsRepository.getAccounts().map { accounts ->
            accounts.filter { account -> account.type == accountType }
        }
    }

}