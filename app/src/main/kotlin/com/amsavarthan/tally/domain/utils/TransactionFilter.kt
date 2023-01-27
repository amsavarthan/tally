package com.amsavarthan.tally.domain.utils

import com.amsavarthan.tally.domain.entity.TransactionDetail

object TransactionFilter {
    operator fun invoke(transaction: TransactionDetail, query: String): Boolean {
        if (query.isBlank()) return true
        val constraints = listOf(
            transaction.category?.name?.lowercase()?.contains(query),
            transaction.category?.type?.title?.lowercase()?.contains(query),
            transaction.category?.emoji?.lowercase()?.contains(query),
            transaction.account?.name?.lowercase()?.contains(query),
            transaction.account?.type?.title?.lowercase()?.contains(query),
            DateFormatter(
                transaction.localDateTime.date,
                forSearchQuery = true
            ).lowercase().contains(query)
        )
        return constraints.any { it == true }
    }
}