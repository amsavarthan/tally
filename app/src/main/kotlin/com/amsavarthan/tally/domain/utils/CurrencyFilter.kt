package com.amsavarthan.tally.domain.utils

const val MAX_AMOUNT_LIMIT = 1_00_00_000
const val MIN_AMOUNT_LIMIT = 1

fun String.isInValidCurrencyFormat(): Boolean {
    if (contains(".")) {
        if (count { it == '.' } > 1) return false
        if (substringAfter('.').length > 2) return false
    }

    if (endsWith("."))
        plus(0).toDoubleOrNull() ?: return false
    else
        toDoubleOrNull() ?: return false

    return true
}

fun String.toCurrencyInt(): Double? {
    return when {
        isEmpty() -> 0.0
        isInValidCurrencyFormat() -> if (endsWith(".")) plus(0).toDouble() else toDouble()
        else -> null
    }
}