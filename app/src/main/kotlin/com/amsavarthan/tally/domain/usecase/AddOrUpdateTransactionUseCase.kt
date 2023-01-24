package com.amsavarthan.tally.domain.usecase

import com.amsavarthan.tally.domain.repository.TransactionRepository
import javax.inject.Inject

private typealias AddOrUpdateTransactionUseCaseResult = Pair<Boolean, String>

class AddOrUpdateTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
) {
    suspend operator fun invoke(
        id: Long?,
        transactionDetails: TransactionDetails,
    ): AddOrUpdateTransactionUseCaseResult {
        return try {
            val transaction = transactionDetails.toTransaction(id)
            when (id) {
                null -> transactionRepository.insertTransaction(transaction)
                else -> transactionRepository.updateTransaction(transaction)
            }
            true to ""
        } catch (e: IllegalStateException) {
            false to (e.message ?: "Invalid details provided")
        }
    }

}