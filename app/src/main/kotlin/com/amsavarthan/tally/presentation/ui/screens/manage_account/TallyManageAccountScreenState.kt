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
    val cashAmount: Double = 0.0,
    val hasDataChanged: Boolean = false,
    val shouldShowUnsavedChangesAlert: Boolean = false,
    val shouldShowDeleteConfirmationAlert: Boolean = false,
) : Parcelable

@Parcelize
data class DebitCardDetails(
    val name: String = "",
    val outstandingBalance: Double = 0.0,
) : Parcelable

fun DebitCardDetails.toAccount(): Account {
    return Account(
        name = name,
        balance = outstandingBalance,
        type = AccountType.DebitCard
    )
}

@Parcelize
data class CreditDetails(
    val name: String = "",
    val creditLimit: Double = 0.0,
    val outstandingAmount: Double = 0.0,
) : Parcelable

fun CreditDetails.toAccount(type: AccountType): Account {
    return Account(
        name = name,
        balance = outstandingAmount,
        limit = creditLimit,
        type = type
    )
}