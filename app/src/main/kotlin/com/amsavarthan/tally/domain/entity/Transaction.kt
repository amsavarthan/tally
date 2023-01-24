package com.amsavarthan.tally.domain.entity

import android.os.Parcelable
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.amsavarthan.tally.domain.utils.LocalDateTimeParceler
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.serializers.LocalDateTimeIso8601Serializer
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import kotlinx.serialization.Serializable

@Entity(
    tableName = "entry",
    foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = ["id"],
            childColumns = ["account_id"],
            onDelete = CASCADE,
        ),
    ],
    indices = [
        Index(value = ["account_id"])
    ]
)
@Serializable
@Parcelize
data class Transaction(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    val id: Long? = null,
    @ColumnInfo(name = "amount")
    val amount: Double,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    @TypeParceler<LocalDateTime, LocalDateTimeParceler>()
    @ColumnInfo(name = "date_time")
    val dateTime: LocalDateTime,
    @ColumnInfo(name = "category_id")
    val categoryId: Long,
    @ColumnInfo(name = "account_id")
    val accountId: Long,
    @ColumnInfo(name = "transaction_type")
    val transactionType: TransactionType,
) : Parcelable

typealias TransactionType = CategoryType