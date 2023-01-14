package com.amsavarthan.tally.presentation.ui.screens.accounts

import com.amsavarthan.tally.domain.entity.Account

data class TallyAccountsScreenState(
    val accounts: List<Account> = emptyList(),
)