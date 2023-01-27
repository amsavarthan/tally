package com.amsavarthan.tally.presentation.ui.screens.transactions

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.amsavarthan.tally.domain.entity.TransactionDetail
import com.amsavarthan.tally.domain.usecase.GetTransactionsDetailsUseCase
import com.amsavarthan.tally.domain.utils.TransactionFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionsScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getTransactionsDetailsUseCase: GetTransactionsDetailsUseCase,
) : ViewModel() {

    @OptIn(SavedStateHandleSaveableApi::class)
    var uiState by savedStateHandle.saveable { mutableStateOf(TallyTransactionsScreenState()) }
        private set

    private var _events = MutableSharedFlow<UiEvent>(replay = 1)
    val events = _events.asSharedFlow()

    private var allTransactions: List<TransactionDetail> = emptyList()

    init {
        viewModelScope.launch {
            getTransactionsDetailsUseCase().collectLatest { transactions ->
                if (transactions.isEmpty()) {
                    _events.emit(UiEvent.NavigateBack)
                    return@collectLatest
                }
                allTransactions = transactions
                uiState = uiState.copy(transactions = transactions)
            }
        }
    }

    fun onEvent(event: TallyTransactionsScreenEvent) {
        when (event) {
            TallyTransactionsScreenEvent.ToggleSearchBarVisibility -> {
                uiState = uiState.copy(
                    isSearchBarVisible = !uiState.isSearchBarVisible,
                    transactions = allTransactions
                )
                if (uiState.isSearchBarVisible) uiState = uiState.copy(searchQuery = "")
            }
            is TallyTransactionsScreenEvent.OnSearchQueryChanged -> {
                uiState = uiState.copy(searchQuery = event.query)
                viewModelScope.launch(Dispatchers.IO) {
                    val filteredTransactions = allTransactions.filter { transaction ->
                        TransactionFilter(transaction, event.query)
                    }
                    uiState = uiState.copy(transactions = filteredTransactions)
                }
            }
        }
    }

    sealed interface UiEvent {
        object NavigateBack : UiEvent
    }
}
