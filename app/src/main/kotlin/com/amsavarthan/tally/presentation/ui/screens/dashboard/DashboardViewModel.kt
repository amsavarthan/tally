package com.amsavarthan.tally.presentation.ui.screens.dashboard

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.amsavarthan.tally.domain.usecase.GetOnBoardingStatusUseCase
import com.amsavarthan.tally.domain.usecase.GetWalletAmountDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getOnBoardingStatusUseCase: GetOnBoardingStatusUseCase,
    getWalletAmountDetailUseCase: GetWalletAmountDetailUseCase,
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
            getWalletAmountDetailUseCase().onEach { walletDetails ->
                uiState = uiState.copy(
                    outstandingBalanceAmount = walletDetails.outstandingBalance,
                    outstandingRepaymentAmount = walletDetails.repaymentAmount
                )
            }.collect()
        }
    }

}