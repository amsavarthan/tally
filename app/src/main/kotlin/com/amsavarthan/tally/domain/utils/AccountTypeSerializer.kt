package com.amsavarthan.tally.domain.utils

import com.amsavarthan.tally.domain.entity.AccountType
import com.ramcosta.composedestinations.navargs.DestinationsNavTypeSerializer
import com.ramcosta.composedestinations.navargs.NavTypeSerializer

@NavTypeSerializer
class AccountTypeSerializer : DestinationsNavTypeSerializer<AccountType> {
    override fun fromRouteString(routeStr: String): AccountType {
        return AccountType.parse(routeStr)
    }

    override fun toRouteString(value: AccountType): String {
        return value.title
    }
}