package com.amsavarthan.tally.domain.entity


sealed interface ChooserType {
    object Category : ChooserType
    object Account : ChooserType
}
