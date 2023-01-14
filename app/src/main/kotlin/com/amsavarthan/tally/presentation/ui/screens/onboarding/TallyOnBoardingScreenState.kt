package com.amsavarthan.tally.presentation.ui.screens.onboarding

import android.os.Parcelable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AccountBalance
import androidx.compose.material.icons.twotone.AccountBalanceWallet
import androidx.compose.material.icons.twotone.CreditCard
import androidx.compose.ui.graphics.vector.ImageVector
import com.amsavarthan.tally.R
import com.amsavarthan.tally.domain.entity.Account
import kotlinx.parcelize.Parcelize

@Parcelize
data class TallyOnBoardingScreenState(
    val cashAccountId: Long? = null,
    val stepNumber: Int = 0,
    val cashAmount: Double = 0.0,
    val accounts: List<Account> = emptyList(),
    val outstandingBalance: Double = 0.0,
    val repaymentAmount: Double = 0.0,
) : Parcelable {
    val canGoBack: Boolean
        get() = stepNumber != 0
}