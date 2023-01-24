package com.amsavarthan.tally.domain.usecase

import com.amsavarthan.tally.domain.entity.TransactionDetail
import com.amsavarthan.tally.domain.entity.asDetails
import com.amsavarthan.tally.domain.repository.AccountsRepository
import com.amsavarthan.tally.domain.repository.CategoryRepository
import com.amsavarthan.tally.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GetTransactionDetailsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountsRepository,
    private val categoryRepository: CategoryRepository,
    private val getLastSelectedCategoryUseCase: GetLastSelectedCategoryUseCase,
) {

    suspend operator fun invoke(id: Long?): TransactionDetail {
        if (id == null) {
            return TransactionDetail(
                account = accountRepository.getCashAccount().firstOrNull(),
                category = getLastSelectedCategoryUseCase(),
            )
        }

        val transaction = transactionRepository.getTransaction(id)
        val account = accountRepository.getAccount(transaction.accountId)
        val category = categoryRepository.getCategory(transaction.categoryId)

        return transaction.asDetails(
            category = category,
            account = account
        )

    }

}