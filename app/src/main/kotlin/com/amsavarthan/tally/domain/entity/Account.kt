package com.amsavarthan.tally.domain.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "account",
    indices = [
        Index(
            value = ["name", "type"],
            unique = true
        )
    ],
)
@Parcelize
data class Account(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    val id: Long? = null,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "balance")
    val balance: Double = 0.0,
    @ColumnInfo(name = "type")
    val type: AccountType,
    @ColumnInfo(name = "limit")
    val limit: Double = 0.0,
) : Parcelable {
    fun isValid(): Boolean {
        return when (type) {
            AccountType.Cash, AccountType.DebitCard -> name.isNotBlank() && name.length > 2 && balance >= 0
            AccountType.CreditCard, AccountType.PayLater -> name.isNotBlank() && name.length > 2 && balance >= 0 && limit >= balance
        }
    }
}