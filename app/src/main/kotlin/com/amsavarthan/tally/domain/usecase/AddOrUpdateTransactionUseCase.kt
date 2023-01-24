package com.amsavarthan.tally.domain.usecase

import com.amsavarthan.tally.domain.entity.TransactionDetail
import com.amsavarthan.tally.domain.entity.toTransaction
import com.amsavarthan.tally.domain.repository.TransactionRepository
import com.amsavarthan.tally.domain.utils.MIN_AMOUNT_LIMIT
import com.amsavarthan.tally.domain.utils.UseCaseResult
import javax.inject.Inject


class AddOrUpdateTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
) {
    suspend operator fun invoke(transactionDetails: TransactionDetail): UseCaseResult {
        return try {
            val transaction = transactionDetails.toTransaction()
            if (transaction.amount < MIN_AMOUNT_LIMIT) throw IllegalStateException("Minimum transaction amount is ₹1")
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