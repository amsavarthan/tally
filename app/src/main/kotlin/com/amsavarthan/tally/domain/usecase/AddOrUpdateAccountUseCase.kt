package com.amsavarthan.tally.domain.usecase

import android.database.sqlite.SQLiteConstraintException
import com.amsavarthan.tally.domain.entity.Account
import com.amsavarthan.tally.domain.entity.AccountType
import com.amsavarthan.tally.domain.repository.AccountsRepository
import com.amsavarthan.tally.domain.utils.UseCaseResultWithData
import javax.inject.Inject

class AddOrUpdateAccountUseCase @Inject constructor(
    private val accountsRepository: AccountsRepository,
) {

    suspend operator fun invoke(account: Account): UseCaseResultWithData<Long?> {

        if (account.name.isBlank()) return null to (false to "Please enter a valid account name")
        if (account.balance < 0) return null to (false to "Amount cannot be less than 0")

        val isCreditOrPayLater =
            account.type == AccountType.CreditCard || account.type == AccountType.PayLater
        if ((isCreditOrPayLater) && account.balance > account.limit) return null to (false to "Repayment amount exceeds credit limit")

        if (!account.isValid()) return null to (false to "Invalid account details entered")

        return try {
            val accountId = when (account.id) {
                null -> accountsRepository.insertAccount(account)
                else -> {
                    accountsRepository.updateAccount(account)
                    account.id
                }
            }
            accountId to (true to "")
        } catch (e: SQLiteConstraintException) {
            null to (false to "Account already exists")
        }
    }

}