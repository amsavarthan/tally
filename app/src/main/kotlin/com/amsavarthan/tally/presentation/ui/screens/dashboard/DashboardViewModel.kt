package com.amsavarthan.tally.presentation.ui.screens.dashboard

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.amsavarthan.tally.domain.usecase.GetCurrentWeekTransactionsAndAmountSpent
import com.amsavarthan.tally.domain.usecase.GetOnBoardingStatusUseCase
import com.amsavarthan.tally.domain.usecase.GetWalletAmountDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getOnBoardingStatusUseCase: GetOnBoardingStatusUseCase,
    getWalletAmountDetailUseCase: GetWalletAmountDetailUseCase,
    getCurrentWeekTransactionsAndAmountSpent: GetCurrentWeekTransactionsAndAmountSpent,
) : ViewModel() {

    val hasUserOnBoarded = getOnBoardingStatusUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    @OptIn(SavedStateHandleSaveableApi::class)
    var uiState by savedStateHandle.saveable { mutableStateOf(TallyDashboardScreenState()) }
        private set

    init {
        viewModelScope.launch {
            getCurrentWeekTransactionsAndAmountSpent().collectLatest { details ->
                uiState = uiState.copy(
                    transactions = details.transactions,
                    spentThisWeek = details.amountSpent
                )
            }
        }
    }

    init {
        viewModelScope.launch {
            getWalletAmountDetailUseCase().collectLatest { walletDetails ->
                uiState = uiState.copy(
                    outstandingBalanceAmount = walletDetails.outstandingBalance,
                    outstandingRepaymentAmount = walletDetails.repaymentAmount
                )
            }
        }
    }

}