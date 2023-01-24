package com.amsavarthan.tally.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
sealed class CategoryType(val title: String) : Parcelable {
    object Income : CategoryType("Income")
    object Expense : CategoryType("Expense")
    companion object {

        fun getAllCategoryTypes() = listOf(Income, Expense)

        fun parse(title: String): CategoryType {
            return when (title.lowercase()) {
                Income.title.lowercase() -> Income
                Expense.title.lowercase() -> Expense
                else -> throw IllegalArgumentException("Corresponding category type not found for $title")
            }
        }
    }
}