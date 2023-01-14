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
data class TallyEntry(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    val id: Long? = null,
    @ColumnInfo(name = "amount")
    val amount: Double,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    @TypeParceler<LocalDateTime, LocalDateTimeParceler>()
    @ColumnInfo(name = "date_time")
    val dateTime: LocalDateTime,
    @Embedded(prefix = "category_")
    val category: Category,
    @ColumnInfo(name = "account_id")
    val accountId: Long,
) : Parcelable