package com.amsavarthan.tally.presentation.ui.screens.manage_account

import android.os.Parcelable
import com.amsavarthan.tally.domain.entity.Account
import com.amsavarthan.tally.domain.entity.AccountType
import kotlinx.parcelize.Parcelize

@Parcelize
data class TallyAddEditAccountScreenState(
    val accountId: Long?,
    val debitCardDetails: DebitCardDetails = DebitCardDetails(),
    val creditCardDetails: CreditDetails = CreditDetails(),
    val payLaterAccountDetails: CreditDetails = CreditDetails(),
    val cashAmount: String = "",
    val hasDataChanged: Boolean = false,
    val shouldShowUnsavedChangesAlert: Boolean = false,
    val shouldShowDeleteConfirmationAlert: Boolean = false,
) : Parcelable

@Parcelize
data class DebitCardDetails(
    val name: String = "",
    val outstandingBalance: String = "",
) : Parcelable

fun DebitCardDetails.toAccount(): Account {
    return Account(
        name = name,
        balance = if (outstandingBalance.isEmpty()) 0.0 else outstandingBalance.toDouble(),
        type = AccountType.DebitCard
    )
}

@Parcelize
data class CreditDetails(
    val name: String = "",
    val creditLimit: String = "",
    val outstandingAmount: String = "",
) : Parcelable

fun CreditDetails.toAccount(type: AccountType): Account {
    return Account(
        name = name,
        balance = if (outstandingAmount.isEmpty()) 0.0 else outstandingAmount.toDouble(),
        limit = creditLimit.toDouble(),
        type = type
    )
}