package com.amsavarthan.tally.domain.usecase

import com.amsavarthan.tally.domain.entity.WeekTransactionDetail
import com.amsavarthan.tally.domain.entity.asDetails
import com.amsavarthan.tally.domain.repository.AccountsRepository
import com.amsavarthan.tally.domain.repository.CategoryRepository
import com.amsavarthan.tally.domain.repository.TransactionRepository
import com.amsavarthan.tally.domain.utils.CurrencyFormatter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetCurrentWeekTransactionsAndAmountSpent @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val accountsRepository: AccountsRepository,
) {

    operator fun invoke(): Flow<WeekTransactionDetail> {
        return combine(
            transactionRepository.getCurrentWeekTransactions(),
            transactionRepository.getAmountSpentInCurrentWeek()
        ) { transactions, amount ->

            val transactionDetails = transactions.map { transaction ->
                val category = categoryRepository.getCategory(transaction.categoryId)
                val account = accountsRepository.getAccount(transaction.accountId)
                return@map transaction.asDetails(category = category, account = account)
            }

            return@combine WeekTransactionDetail(
                transactions = transactionDetails,
                amountSpent = CurrencyFormatter(amount, returnBlankIfZero = false)
            )
        }
    }

}