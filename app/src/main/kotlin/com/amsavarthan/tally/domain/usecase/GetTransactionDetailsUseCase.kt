package com.amsavarthan.tally.domain.usecase

import com.amsavarthan.tally.domain.entity.Transaction
import com.amsavarthan.tally.domain.repository.AccountsRepository
import com.amsavarthan.tally.domain.repository.CategoryRepository
import com.amsavarthan.tally.domain.repository.TransactionRepository
import com.amsavarthan.tally.domain.utils.CurrencyFormatter
import com.amsavarthan.tally.presentation.ui.screens.manage_transaction.TallyManageTransactionScreenState
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

typealias TransactionDetails = TallyManageTransactionScreenState

@Throws(IllegalStateException::class)
fun TransactionDetails.toTransaction(id: Long?): Transaction {

    val amount = amount.toDoubleOrNull()
    checkNotNull(amount) {
        "Invalid amount entered"
    }

    checkNotNull(account) {
        "Please choose an account for this transaction"
    }

    checkNotNull(category) {
        "Please choose a category for this transaction"
    }

    return Transaction(
        id = id,
        amount = amount,
        dateTime = localDateTime,
        categoryId = category.id!!,
        accountId = account.id!!,
        transactionType = category.type,
    )

}

class GetTransactionDetailsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountsRepository,
    private val categoryRepository: CategoryRepository,
    private val getLastSelectedCategoryUseCase: GetLastSelectedCategoryUseCase,
) {

    suspend operator fun invoke(id: Long?): TransactionDetails {
        if (id == null) {
            return TransactionDetails(
                account = accountRepository.getCashAccount().firstOrNull(),
                category = getLastSelectedCategoryUseCase(),
            )
        }

        val transaction = transactionRepository.getTransaction(id)
        val account = accountRepository.getAccount(transaction.accountId)
        val category = categoryRepository.getCategory(transaction.categoryId)

        return TransactionDetails(
            amount = CurrencyFormatter(transaction.amount, returnBlankIfZero = false),
            localDateTime = transaction.dateTime,
            account = account,
            category = category
        )

    }

}