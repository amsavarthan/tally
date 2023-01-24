package com.amsavarthan.tally.domain.entity

import android.os.Parcelable
import com.amsavarthan.tally.domain.utils.CurrencyFormatter
import com.amsavarthan.tally.domain.utils.LocalDateTimeParceler
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.serializers.LocalDateTimeIso8601Serializer
import kotlinx.datetime.toLocalDateTime
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import kotlinx.serialization.Serializable

@Parcelize
data class TransactionDetail(
    val transactionId: Long? = null,
    val amount: String = "",
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    @TypeParceler<LocalDateTime, LocalDateTimeParceler>()
    val localDateTime: LocalDateTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()),
    val account: Account? = null,
    val category: Category? = null,
) : Parcelable

@Throws(IllegalStateException::class)
fun TransactionDetail.toTransaction(): Transaction {

    val amount = amount.toDoubleOrNull()
    checkNotNull(amount) {
        "Invalid amount entered"
    }

    checkNotNull(account) {
        "Please choose an account for this transaction"
    }

    checkNotNull(category) {
        "Please choose a category for this transaction"
    }

    return Transaction(
        id = transactionId,
        amount = amount,
        dateTime = localDateTime,
        categoryId = category.id!!,
        accountId = account.id!!,
        transactionType = category.type,
    )

}

fun Transaction.asDetails(
    category: Category,
    account: Account,
): TransactionDetail {
    return TransactionDetail(
        transactionId = id,
        amount = CurrencyFormatter(amount, returnBlankIfZero = false),
        localDateTime = dateTime,
        category = category,
        account = account,
    )
}