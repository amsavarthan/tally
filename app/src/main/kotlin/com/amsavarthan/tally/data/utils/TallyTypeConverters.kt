package com.amsavarthan.tally.data.utils

import androidx.room.TypeConverter
import com.amsavarthan.tally.domain.entity.AccountType
import com.amsavarthan.tally.domain.entity.CategoryType
import kotlinx.datetime.LocalDateTime

object TallyTypeConverters {

    @TypeConverter
    fun toString(localDateTime: LocalDateTime): String = localDateTime.toString()

    @TypeConverter
    fun toLocalDateTime(dateTimeString: String): LocalDateTime = LocalDateTime.parse(dateTimeString)

    @TypeConverter
    fun toString(categoryType: CategoryType): String = categoryType.title.lowercase()

    @TypeConverter
    fun toCategoryType(categoryTitle: String): CategoryType = CategoryType.parse(categoryTitle)

    @TypeConverter
    fun toString(accountType: AccountType): String = accountType.title.lowercase()

    @TypeConverter
    fun toAccountType(accountTitle: String): AccountType = AccountType.parse(accountTitle)

}