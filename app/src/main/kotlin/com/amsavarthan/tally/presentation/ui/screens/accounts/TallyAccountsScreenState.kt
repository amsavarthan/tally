package com.amsavarthan.tally.presentation.ui.screens.accounts

import android.os.Parcelable
import com.amsavarthan.tally.domain.entity.Account
import kotlinx.parcelize.Parcelize

@Parcelize
data class TallyAccountsScreenState(
    val accounts: List<Account> = emptyList(),
) : Parcelable