package com.amsavarthan.tally.domain.utils

import kotlinx.datetime.*

object DateFormatter {

    operator fun invoke(date: LocalDate): String {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val isToday = today == date
        val isYesterday = today.minus(DatePeriod(days = 1)) == date

        return when {
            isToday -> "Today"
            isYesterday -> "Yesterday"
            else -> "${date.dayOfMonth.ordinal()} ${date.month.name[0]}${date.month.name.drop(1).lowercase()}, ${date.year}"
        }
    }

    private fun Int.ordinal() = "$this'" + when {
        (this % 100 in 11..13) -> "th"
        (this % 10) == 1 -> "st"
        (this % 10) == 2 -> "nd"
        (this % 10) == 3 -> "rd"
        else -> "th"
    }

}