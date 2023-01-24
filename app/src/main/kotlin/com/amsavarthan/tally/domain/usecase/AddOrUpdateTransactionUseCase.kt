package com.amsavarthan.tally.domain.usecase

import com.amsavarthan.tally.domain.entity.TransactionDetail
import com.amsavarthan.tally.domain.entity.toTransaction
import com.amsavarthan.tally.domain.repository.AccountsRepository
import com.amsavarthan.tally.domain.repository.TransactionRepository
import javax.inject.Inject

private typealias AddOrUpdateTransactionUseCaseResult = Pair<Boolean, String>

class AddOrUpdateTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
) {
    suspend operator fun invoke(transactionDetails: TransactionDetail): AddOrUpdateTransactionUseCaseResult {
        return try {
            val transaction = transactionDetails.toTransaction()
            when (transaction.id) {
                null -> transactionRepository.insertTransaction(transaction)
                else -> transactionRepository.updateTransaction(transaction)
            }
            true to ""
        } catch (e: IllegalStateException) {
            false to (e.message ?: "Invalid details provided")
        }
    }

}