package com.amsavarthan.tally.presentation.ui.screens.onboarding

import android.os.Parcelable
import com.amsavarthan.tally.domain.entity.Account
import kotlinx.parcelize.Parcelize

@Parcelize
data class TallyOnBoardingScreenState(
    val cashAccountId: Long? = null,
    val stepNumber: Int = 0,
    val accounts: List<Account> = emptyList(),
    val cashAmount: String = "",
    val outstandingBalance: String = "",
    val repaymentAmount: String = "",
) : Parcelable {
    val canGoBack: Boolean
        get() = stepNumber != 0
}