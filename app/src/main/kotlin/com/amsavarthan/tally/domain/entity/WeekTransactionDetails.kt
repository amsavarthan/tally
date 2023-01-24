package com.amsavarthan.tally.domain.entity

import android.os.Parcelable
import com.amsavarthan.tally.presentation.ui.screens.dashboard.Transactions
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeekTransactionDetails(
    val transactions: Transactions = emptyList(),
    val amountSpent: String = "",
) : Parcelable