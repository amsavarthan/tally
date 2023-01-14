package com.amsavarthan.tally.presentation.ui.screens.wallet

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.amsavarthan.tally.domain.usecase.GetWalletAmountDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getWalletAmountDetailUseCase: GetWalletAmountDetailUseCase,
) : ViewModel() {

    @OptIn(SavedStateHandleSaveableApi::class)
    var uiState by savedStateHandle.saveable { mutableStateOf(TallyWalletScreenState()) }
        private set

    init {
        viewModelScope.launch {
            getWalletAmountDetailUseCase().onEach { walletDetails ->
                uiState = uiState.copy(walletAmountDetail = walletDetails)
            }.collect()
        }
    }

}