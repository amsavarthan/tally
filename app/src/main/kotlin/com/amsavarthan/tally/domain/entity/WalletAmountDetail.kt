package com.amsavarthan.tally.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WalletAmountDetail(
    val cashHoldings: String = "",
    val outstandingBalance: String = "",
    val repaymentAmount: String = "",
) : Parcelable