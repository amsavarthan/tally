package com.amsavarthan.tally.domain.usecase

import com.amsavarthan.tally.domain.repository.TransactionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetTotalTransactionCount @Inject constructor(
    private val transactionRepository: TransactionRepository,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke() = transactionRepository.getTransactions().mapLatest { it.count() }

}