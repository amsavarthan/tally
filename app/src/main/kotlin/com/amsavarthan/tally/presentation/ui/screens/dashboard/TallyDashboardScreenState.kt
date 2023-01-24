package com.amsavarthan.tally.presentation.ui.screens.dashboard

import android.os.Parcelable
import com.amsavarthan.tally.domain.entity.TransactionDetail
import kotlinx.parcelize.Parcelize

@Parcelize
data class TallyDashboardScreenState(
    val outstandingBalanceAmount: String = "",
    val outstandingRepaymentAmount: String = "",
    val transactions: List<TransactionDetail> = emptyList(),
    val spentThisWeek: String = "",
) : Parcelable
