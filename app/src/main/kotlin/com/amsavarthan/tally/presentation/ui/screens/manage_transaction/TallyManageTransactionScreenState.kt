package com.amsavarthan.tally.presentation.ui.screens.manage_transaction

import android.os.Parcelable
import com.amsavarthan.tally.domain.entity.TransactionDetail
import kotlinx.parcelize.Parcelize

@Parcelize
data class TallyManageTransactionScreenState(
    val transactionDetail: TransactionDetail = TransactionDetail(),
    val shouldShowDeleteConfirmationAlertDialog: Boolean = false,
) : Parcelable