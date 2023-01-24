package com.amsavarthan.tally.domain.usecase

import com.amsavarthan.tally.domain.entity.WalletAmountDetail
import com.amsavarthan.tally.domain.repository.AccountsRepository
import com.amsavarthan.tally.domain.utils.CurrencyFormatter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetWalletAmountDetailUseCase @Inject constructor(
    private val accountsRepository: AccountsRepository,
) {
    operator fun invoke(): Flow<WalletAmountDetail> {
        return combine(
            accountsRepository.getOutstandingRepaymentAmount(),
            accountsRepository.getOutstandingBalanceAmount(),
            accountsRepository.getCashAccount().map { it?.balance }
        ) { repaymentAmount, balanceAmount, cashBalance ->
            return@combine WalletAmountDetail(
                cashHoldings = CurrencyFormatter(cashBalance ?: 0.0, returnBlankIfZero = false),
                repaymentAmount = CurrencyFormatter(repaymentAmount, returnBlankIfZero = false),
                outstandingBalance = CurrencyFormatter(balanceAmount, returnBlankIfZero = false)
            )
        }
    }

}