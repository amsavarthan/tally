package com.amsavarthan.tally.domain.utils

object CurrencyFilter {

    operator fun invoke(input: String): String {
        if (!input.contains(".")) {
            return input.take(7)
        }

        val filtered = input.removeAllExceptFirst(".")

        val digitsBeforeDecimal = filtered.substringBefore(".").take(7)
        val digitsAfterDecimal = filtered.substringAfter(".").take(2)

        if (digitsAfterDecimal.isBlank()) return digitsBeforeDecimal
        return "$digitsBeforeDecimal.$digitsAfterDecimal"
    }

    private fun String.removeAllExceptFirst(
        value: String,
        tempReplacementValue: String = "#",
    ): String {
        return this.replaceFirst(value, tempReplacementValue)
            .replace(value, "")
            .replaceFirst(tempReplacementValue, value)
    }

}