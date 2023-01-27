package com.amsavarthan.tally.presentation.ui.screens.dashboard

import android.os.Parcelable
import com.amsavarthan.tally.domain.entity.WeekTransactionDetail
import kotlinx.parcelize.Parcelize

@Parcelize
data class TallyDashboardScreenState(
    val outstandingBalanceAmount: String = "",
    val outstandingRepaymentAmount: String = "",
    val weekTransactionDetail: WeekTransactionDetail = WeekTransactionDetail(),
    val totalTransactions: Int? = null,
) : Parcelable
