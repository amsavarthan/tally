package com.amsavarthan.tally.domain.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Entity(
    tableName = "category",
    primaryKeys = ["name", "type"],
)
@Serializable
@Parcelize
data class Category(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "emoji")
    val emoji: String,
    @ColumnInfo(name = "type")
    val type: CategoryType,
) : Parcelable

@Serializable
@Parcelize
sealed class CategoryType(val title: String) : Parcelable {
    object Income : CategoryType("Income")
    object Expense : CategoryType("Expense")
    companion object {
        fun parse(title: String): CategoryType {
            return when (title.lowercase()) {
                Income.title.lowercase() -> Income
                Expense.title.lowercase() -> Expense
                else -> throw IllegalArgumentException("Corresponding category type not found for $title")
            }
        }
    }
}