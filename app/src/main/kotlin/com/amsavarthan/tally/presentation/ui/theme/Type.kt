package com.amsavarthan.tally.presentation.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.amsavarthan.tally.R

val fonts = FontFamily(
    Font(R.font.light, weight = FontWeight.Light),
    Font(R.font.regular),
    Font(R.font.medium, weight = FontWeight.Medium),
    Font(R.font.semibold, weight = FontWeight.SemiBold),
    Font(R.font.bold, weight = FontWeight.Bold),
)

// Set of Material typography styles to start with
val Typography = Typography(
    defaultFontFamily = fonts,
)