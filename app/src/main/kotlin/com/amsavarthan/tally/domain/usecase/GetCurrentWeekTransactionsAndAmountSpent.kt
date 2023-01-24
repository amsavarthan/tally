package com.amsavarthan.tally.domain.usecase

import com.amsavarthan.tally.domain.entity.WeekTransactionDetails
import com.amsavarthan.tally.domain.repository.TransactionRepository
import com.amsavarthan.tally.domain.utils.CurrencyFormatter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetCurrentWeekTransactionsAndAmountSpent @Inject constructor(
    private val transactionRepository: TransactionRepository,
) {

    operator fun invoke(): Flow<WeekTransactionDetails> {
        return combine(
            transactionRepository.getCurrentWeekTransactions(),
            transactionRepository.getAmountSpentInCurrentWeek()
        ) { transactions, amount ->
            return@combine WeekTransactionDetails(
                transactions = transactions,
                amountSpent = CurrencyFormatter(amount, returnBlankIfZero = false)
            )
        }
    }

}