package com.amsavarthan.tally.presentation.ui.screens.dashboard

import android.os.Parcelable
import com.amsavarthan.tally.domain.entity.Transaction
import kotlinx.parcelize.Parcelize

typealias Transactions = List<Transaction>

@Parcelize
data class TallyDashboardScreenState(
    val outstandingBalanceAmount: String = "",
    val outstandingRepaymentAmount: String = "",
    val transactions: Transactions = emptyList(),
    val spentThisWeek: String = "",
) : Parcelable
