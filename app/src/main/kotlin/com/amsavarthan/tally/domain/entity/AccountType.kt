package com.amsavarthan.tally.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class AccountType(val title: String) : Parcelable {
    object Cash : AccountType("Cash")
    object DebitCard : AccountType("Debit Card")
    object CreditCard : AccountType("Credit Card")
    object PayLater : AccountType("Pay Later Account")
    companion object {

        fun getAllAccountTypes() = listOf(
            Cash, DebitCard, CreditCard, PayLater
        )

        fun parse(title: String?): AccountType {
            return when (title?.lowercase()) {
                Cash.title.lowercase() -> Cash
                DebitCard.title.lowercase() -> DebitCard
                CreditCard.title.lowercase() -> CreditCard
                PayLater.title.lowercase() -> PayLater
                else -> throw IllegalArgumentException("Corresponding account type not found for $title")
            }
        }
    }
}