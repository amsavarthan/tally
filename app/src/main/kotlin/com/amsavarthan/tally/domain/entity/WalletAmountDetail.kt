package com.amsavarthan.tally.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WalletAmountDetail(
    val cashHoldings: Double = 0.0,
    val outstandingBalance: Double = 0.0,
    val repaymentAmount: Double = 0.0,
) : Parcelable