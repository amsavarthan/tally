package com.amsavarthan.tally.presentation.ui.screens.onboarding

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.amsavarthan.tally.domain.entity.Account
import com.amsavarthan.tally.domain.entity.AccountType
import com.amsavarthan.tally.domain.entity.TallyOnBoardingStep
import com.amsavarthan.tally.domain.repository.AccountsRepository
import com.amsavarthan.tally.domain.repository.AppDataRepository
import com.amsavarthan.tally.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(SavedStateHandleSaveableApi::class)
class OnBoardingScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getWalletAmountDetailUseCase: GetWalletAmountDetailUseCase,
    private val updateCashAccountBalanceUseCase: UpdateCashAccountBalanceUseCase,
    private val accountsRepository: AccountsRepository,
    private val appDataRepository: AppDataRepository,
) : ViewModel() {

    var uiState by savedStateHandle.saveable { mutableStateOf(TallyOnBoardingScreenState()) }
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            accountsRepository.getAccounts().onEach { accounts ->
                uiState = uiState.copy(accounts = accounts)
            }.collect()
        }
    }

    init {
        viewModelScope.launch {
            accountsRepository.getCashAccount().onEach { cashAccount ->
                if (cashAccount != null) {
                    uiState = uiState.copy(cashAccountId = cashAccount.id)
                    return@onEach
                }
                accountsRepository.insertAccount(
                    Account(
                        name = AccountType.Cash.title,
                        type = AccountType.Cash,
                        balance = 0.0,
                    )
                )
            }.collect()
        }
    }

    init {
        viewModelScope.launch {
            getWalletAmountDetailUseCase().onEach { walletDetails ->
                uiState = uiState.copy(
                    outstandingBalance = walletDetails.outstandingBalance,
                    repaymentAmount = walletDetails.repaymentAmount,
                    cashAmount = walletDetails.cashHoldings
                )
            }.collect()
        }
    }

    fun onEvent(event: TallyOnBoardingScreenEvent) {
        when (event) {
            TallyOnBoardingScreenEvent.OnActionButtonClicked -> {
                viewModelScope.launch {
                    if (TallyOnBoardingStep.isLastStep(uiState.stepNumber)) {
                        appDataRepository.updateOnBoardingState(hasOnBoarded = true)
                        _eventFlow.emit(UiEvent.NavigateToDashboard)
                        return@launch
                    }
                    goForward()
                }
            }
            TallyOnBoardingScreenEvent.OnBackPressed -> goBack()
            is TallyOnBoardingScreenEvent.OnCashAmountEntered -> {
                viewModelScope.launch {
                    val amountAsDouble = event.amount.toDoubleOrNull() ?: return@launch
                    updateCashAccountBalanceUseCase(uiState.cashAccountId, amountAsDouble)
                }
            }
        }
    }

    private fun goForward() {
        uiState = uiState.copy(stepNumber = uiState.stepNumber.inc())
    }

    private fun goBack() {
        uiState = uiState.copy(stepNumber = uiState.stepNumber.dec())
    }

    sealed class UiEvent {
        object NavigateToDashboard : UiEvent()
    }

}