package com.amsavarthan.tally.presentation.ui.screens.transactions

import android.os.Parcelable
import com.amsavarthan.tally.domain.entity.TransactionDetail
import kotlinx.parcelize.Parcelize

@Parcelize
data class TallyTransactionsScreenState(
    val transactions: List<TransactionDetail> = emptyList(),
    val isSearchBarVisible: Boolean = false,
    val searchQuery: String = "",
) : Parcelable