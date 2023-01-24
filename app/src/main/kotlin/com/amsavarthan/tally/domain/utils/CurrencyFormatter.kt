package com.amsavarthan.tally.domain.utils

import java.text.DecimalFormat

object CurrencyFormatter {

    private val formatter = DecimalFormat("0.00")

    operator fun invoke(number: Double, returnBlankIfZero: Boolean = true): String {
        if (number == 0.0 && returnBlankIfZero) return ""
        return formatter.format(number)
    }

}