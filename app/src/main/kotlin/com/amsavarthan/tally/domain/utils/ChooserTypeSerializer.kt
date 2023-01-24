package com.amsavarthan.tally.domain.utils

import com.amsavarthan.tally.domain.entity.ChooserType
import com.ramcosta.composedestinations.navargs.DestinationsNavTypeSerializer
import com.ramcosta.composedestinations.navargs.NavTypeSerializer

@NavTypeSerializer
class ChooserTypeSerializer : DestinationsNavTypeSerializer<ChooserType> {
    override fun fromRouteString(routeStr: String): ChooserType {
        return when (routeStr) {
            "account" -> ChooserType.Account
            "category" -> ChooserType.Category
            else -> throw IllegalArgumentException("Invalid chooser type")
        }
    }

    override fun toRouteString(value: ChooserType): String {
        return when (value) {
            ChooserType.Account -> "account"
            ChooserType.Category -> "category"
        }
    }
}