package com.amsavarthan.tally.presentation.ui.screens.manage_transaction

import android.os.Parcelable
import com.amsavarthan.tally.domain.entity.Account
import com.amsavarthan.tally.domain.entity.Category
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
data class TallyManageTransactionScreenState(
    val amount: String = "",
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    @TypeParceler<LocalDateTime, LocalDateTimeParceler>()
    val localDateTime: LocalDateTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()),
    val account: Account? = null,
    val category: Category? = null,
) : Parcelable