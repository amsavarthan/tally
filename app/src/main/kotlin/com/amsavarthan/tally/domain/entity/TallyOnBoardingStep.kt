package com.amsavarthan.tally.domain.entity

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AccountBalance
import androidx.compose.material.icons.twotone.AccountBalanceWallet
import androidx.compose.material.icons.twotone.CreditCard
import androidx.compose.ui.graphics.vector.ImageVector
import com.amsavarthan.tally.R

sealed class TallyOnBoardingStep(
    val number: Int,
    val icon: ImageVector,
    val title: String,
    val description: String,
    val actionText: String,
    val illustrationResource: Int? = null,
) {
    object Initial : TallyOnBoardingStep(
        number = 0,
        icon = Icons.TwoTone.AccountBalance,
        title = "All at one place",
        description = "For tracking all of your balances at one place you need to setup your holdings.",
        actionText = "Setup your Wallet",
        illustrationResource = R.drawable.illustration_man_in_park,
    )

    object CashSetup : TallyOnBoardingStep(
        number = 1,
        icon = Icons.TwoTone.AccountBalanceWallet,
        title = "Cash",
        description = "Enter the amount of cash you currently have in your wallet.",
        actionText = "Next",
    )

    object DebitCardSetup : TallyOnBoardingStep(
        number = 2,
        icon = Icons.TwoTone.CreditCard,
        title = "Debit Cards",
        description = "Enter the details of your debit cards you currently have in your wallet.",
        actionText = "Next",
    )

    object CreditCardSetup : TallyOnBoardingStep(
        number = 3,
        icon = Icons.TwoTone.CreditCard,
        title = "Credit Cards",
        description = "Enter the details of your credit cards you currently have in your wallet.",
        actionText = "Next",
    )

    object PayLaterSetup : TallyOnBoardingStep(
        number = 4,
        icon = Icons.TwoTone.AccountBalanceWallet,
        title = "Pay Later Accounts",
        description = "Enter the details of pay later accounts if you have any.",
        actionText = "Next",
    )

    object Summary : TallyOnBoardingStep(
        number = 5,
        icon = Icons.TwoTone.CreditCard,
        title = "Your Wallet",
        description = "Here is the summary of your wallet. You can always edit your holdings later.",
        actionText = "That's Great",
    )

    companion object {
        fun getStepByNumber(number: Int): TallyOnBoardingStep {
            return when (number) {
                Initial.number -> Initial
                CashSetup.number -> CashSetup
                DebitCardSetup.number -> DebitCardSetup
                CreditCardSetup.number -> CreditCardSetup
                PayLaterSetup.number -> PayLaterSetup
                Summary.number -> Summary
                else -> throw IllegalArgumentException("Invalid step number: $number")
            }
        }

        fun isLastStep(number: Int) = number == Summary.number
    }


}