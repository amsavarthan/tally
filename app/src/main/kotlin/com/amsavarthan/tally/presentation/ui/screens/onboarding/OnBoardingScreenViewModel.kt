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
import com.amsavarthan.tally.domain.utils.CurrencyFormatter
import com.amsavarthan.tally.domain.utils.MAX_AMOUNT_LIMIT
import com.amsavarthan.tally.domain.utils.toCurrencyInt
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

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            accountsRepository.getAccounts().collectLatest { accounts ->
                uiState = uiState.copy(accounts = accounts)
            }
        }
    }

    init {
        viewModelScope.launch {
            val cashAccount = accountsRepository.getCashAccount().first()
            val cashAccountId = if (cashAccount != null) cashAccount.id else {
                accountsRepository.insertAccount(
                    Account(
                        name = AccountType.Cash.title,
                        type = AccountType.Cash,
                        balance = 0.0,
                    )
                )
            }
            uiState = uiState.copy(
                cashAccountId = cashAccountId,
                cashAmount = CurrencyFormatter(cashAccount?.balance ?: 0.0)
            )
        }
    }

    init {
        viewModelScope.launch {
            getWalletAmountDetailUseCase().collectLatest { walletDetails ->
                uiState = uiState.copy(
                    outstandingBalance = walletDetails.outstandingBalance,
                    repaymentAmount = walletDetails.repaymentAmount,
                )
            }
        }
    }

    fun onEvent(event: TallyOnBoardingScreenEvent) {
        when (event) {
            TallyOnBoardingScreenEvent.OnActionButtonClicked -> {
                viewModelScope.launch {
                    if (TallyOnBoardingStep.isLastStep(uiState.stepNumber)) {
                        appDataRepository.setOnBoardingState(hasOnBoarded = true)
                        _events.emit(UiEvent.NavigateToDashboard)
                        return@launch
                    }
                    goForward()
                }
            }
            TallyOnBoardingScreenEvent.OnBackPressed -> goBack()
            is TallyOnBoardingScreenEvent.OnCashAmountEntered -> {
                viewModelScope.launch {
                    val amount = event.amount.toCurrencyInt() ?: return@launch
                    if (amount > MAX_AMOUNT_LIMIT) {
                        _events.emit(UiEvent.ShowToast("Value cannot be more than ₹1,00,00,000"))
                        return@launch
                    }
                    uiState = uiState.copy(cashAmount = event.amount)
                    updateCashAccountBalanceUseCase(uiState.cashAccountId, amount)
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

    sealed interface UiEvent {
        data class ShowToast(val message: String) : UiEvent
        object NavigateToDashboard : UiEvent
    }

}