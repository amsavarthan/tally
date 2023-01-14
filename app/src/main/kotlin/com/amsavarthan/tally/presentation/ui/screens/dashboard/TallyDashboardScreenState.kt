package com.amsavarthan.tally.presentation.ui.screens.dashboard

import android.os.Parcelable
import com.amsavarthan.tally.domain.entity.TallyEntry
import kotlinx.parcelize.Parcelize

typealias Transactions = List<TallyEntry>

@Parcelize
data class TallyDashboardScreenState(
    val outstandingBalanceAmount: Double = 0.0,
    val outstandingRepaymentAmount: Double = 0.0,
    val transactions: Transactions = emptyList(),
) : Parcelable
