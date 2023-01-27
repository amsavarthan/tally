package com.amsavarthan.tally.presentation.ui.screens.transactions

sealed interface TallyTransactionsScreenEvent {

    object ToggleSearchBarVisibility : TallyTransactionsScreenEvent
    data class OnSearchQueryChanged(val query: String) : TallyTransactionsScreenEvent

}