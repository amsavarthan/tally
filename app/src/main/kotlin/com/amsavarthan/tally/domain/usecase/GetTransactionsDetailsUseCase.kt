package com.amsavarthan.tally.domain.usecase

import com.amsavarthan.tally.domain.entity.TransactionDetail
import com.amsavarthan.tally.domain.entity.asDetails
import com.amsavarthan.tally.domain.repository.AccountsRepository
import com.amsavarthan.tally.domain.repository.CategoryRepository
import com.amsavarthan.tally.domain.repository.TransactionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetTransactionsDetailsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountsRepository,
    private val categoryRepository: CategoryRepository,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<TransactionDetail>> {
        return transactionRepository.getTransactions().mapLatest { transactions ->
                return@mapLatest transactions.map { transaction ->
                    val account = accountRepository.getAccount(transaction.accountId)
                    val category = categoryRepository.getCategory(transaction.categoryId)

                    return@map transaction.asDetails(
                        category = category,
                        account = account
                    )
                }
            }
    }

}