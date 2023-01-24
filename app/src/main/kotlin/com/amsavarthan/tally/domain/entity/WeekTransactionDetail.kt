package com.amsavarthan.tally.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeekTransactionDetail(
    val transactions: List<TransactionDetail> = emptyList(),
    val amountSpent: String = "",
) : Parcelable