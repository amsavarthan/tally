package com.amsavarthan.tally.presentation.ui.screens.manage_account

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.amsavarthan.tally.domain.entity.AccountType
import com.amsavarthan.tally.domain.repository.AccountsRepository
import com.amsavarthan.tally.domain.usecase.UpdateCashAccountBalanceUseCase
import com.amsavarthan.tally.presentation.ui.screens.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageAccountViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val updateCashAccountBalanceUseCase: UpdateCashAccountBalanceUseCase,
    private val accountsRepository: AccountsRepository,
) : ViewModel() {

    private val navArgs = savedStateHandle.navArgs<TallyManageAccountScreenNavArgs>()
    val accountType: AccountType = navArgs.accountType

    @OptIn(SavedStateHandleSaveableApi::class)
    var uiState by savedStateHandle.saveable {
        mutableStateOf(
            TallyAddEditAccountScreenState(
                accountId = navArgs.accountId,
            )
        )
    }
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            if (accountType != AccountType.Cash) return@launch

            accountsRepository.getCashAccount().onEach { cashAccount ->
                if (cashAccount == null) return@onEach
                uiState = uiState.copy(
                    accountId = cashAccount.id,
                    cashAmount = cashAccount.balance
                )
            }.collect()
        }
    }

    init {
        viewModelScope.launch {
            //If for adding new account or editing cash then return
            if (uiState.accountId == null || accountType == AccountType.Cash) return@launch

            val account = accountsRepository.getAccount(uiState.accountId!!)
            uiState = when (account.type) {
                AccountType.DebitCard -> uiState.copy(
                    debitCardDetails = DebitCardDetails(
                        name = account.name,
                        outstandingBalance = account.balance,
                    )
                )
                AccountType.CreditCard -> uiState.copy(
                    creditCardDetails = CreditDetails(
                        name = account.name,
                        outstandingAmount = account.balance,
                        creditLimit = account.limit
                    )
                )
                AccountType.PayLater -> uiState.copy(
                    payLaterAccountDetails = CreditDetails(
                        name = account.name,
                        outstandingAmount = account.balance,
                        creditLimit = account.limit
                    )
                )
                AccountType.Cash -> uiState
            }
        }
    }

    fun onEvent(event: TallyManageAccountScreenEvent) {
        when (event) {
            is TallyManageAccountScreenEvent.OnActionButtonClicked -> insertOrUpdateAccount()

            is TallyManageAccountScreenEvent.OnDeleteClicked -> deleteAccount()

            is TallyManageAccountScreenEvent.ToggleUnsavedChangesAlertDialog -> {
                uiState = uiState.copy(shouldShowUnsavedChangesAlert = event.visible)
            }

            is TallyManageAccountScreenEvent.ToggleDeleteConfirmationAlertDialog -> {
                uiState = uiState.copy(shouldShowDeleteConfirmationAlert = event.visible)
            }

            is TallyManageAccountScreenEvent.OnAccountBalanceEntered ->
                updateAccountBalanceAmount(event.balance)

            is TallyManageAccountScreenEvent.OnAccountNameEntered ->
                updateAccountName(event.name)

            is TallyManageAccountScreenEvent.OnAccountLimitEntered ->
                updateAccountLimit(event.limit)
        }
    }

    private fun insertOrUpdateAccount() {
        viewModelScope.launch {
            val account = when (accountType) {
                AccountType.CreditCard -> uiState.creditCardDetails.toAccount(accountType)
                AccountType.DebitCard -> uiState.debitCardDetails.toAccount()
                AccountType.PayLater -> uiState.payLaterAccountDetails.toAccount(accountType)
                else -> null
            }?.copy(id = uiState.accountId) ?: return@launch

            if (!account.isValid()) {
                _eventFlow.emit(UiEvent.ShowToast("Invalid ${accountType.title} details"))
                return@launch
            }

            if (account.id == null) {
                val accountId = accountsRepository.insertAccount(account)
                uiState = uiState.copy(accountId = accountId)
            } else {
                accountsRepository.updateAccount(account)
            }

            uiState = uiState.copy(hasDataChanged = false)
            _eventFlow.emit(UiEvent.NavigateBack)
        }
    }

    private fun deleteAccount() {
        viewModelScope.launch {
            uiState = uiState.copy(shouldShowDeleteConfirmationAlert = false)
            val account = accountsRepository.getAccount(uiState.accountId!!)
            accountsRepository.deleteAccount(account)
            _eventFlow.emit(UiEvent.NavigateBack)
        }
    }

    private fun updateAccountBalanceAmount(balance: String) {
        viewModelScope.launch {
            val amountAsDouble = balance.toDoubleOrNull() ?: return@launch
            uiState = uiState.copy(hasDataChanged = accountType != AccountType.Cash)

            when (accountType) {
                AccountType.Cash -> {
                    uiState = uiState.copy(cashAmount = amountAsDouble)
                    updateCashAccountBalanceUseCase(uiState.accountId, amountAsDouble)
                }
                AccountType.CreditCard -> {
                    uiState = uiState.copy(
                        creditCardDetails = uiState.creditCardDetails.copy(
                            outstandingAmount = amountAsDouble,
                        )
                    )
                }
                AccountType.DebitCard -> {
                    uiState = uiState.copy(
                        debitCardDetails = uiState.debitCardDetails.copy(
                            outstandingBalance = amountAsDouble,
                        )
                    )
                }
                AccountType.PayLater -> {
                    uiState = uiState.copy(
                        payLaterAccountDetails = uiState.payLaterAccountDetails.copy(
                            outstandingAmount = amountAsDouble,
                        )
                    )
                }
            }
        }
    }

    private fun updateAccountName(name: String) {
        uiState = when (accountType) {
            AccountType.CreditCard -> uiState.copy(
                hasDataChanged = true,
                creditCardDetails = uiState.creditCardDetails.copy(
                    name = name,
                )
            )
            AccountType.DebitCard -> uiState.copy(
                hasDataChanged = true,
                debitCardDetails = uiState.debitCardDetails.copy(
                    name = name,
                )
            )
            AccountType.PayLater -> uiState.copy(
                hasDataChanged = true,
                payLaterAccountDetails = uiState.payLaterAccountDetails.copy(
                    name = name,
                )
            )
            AccountType.Cash -> uiState
        }
    }

    private fun updateAccountLimit(limit: String) {
        val limitAsDouble = limit.toDoubleOrNull() ?: return
        uiState = when (accountType) {
            AccountType.CreditCard -> uiState.copy(
                hasDataChanged = true,
                creditCardDetails = uiState.creditCardDetails.copy(
                    creditLimit = limitAsDouble,
                )
            )
            AccountType.PayLater -> uiState.copy(
                hasDataChanged = true,
                payLaterAccountDetails = uiState.payLaterAccountDetails.copy(
                    creditLimit = limitAsDouble,
                )
            )
            AccountType.DebitCard, AccountType.Cash -> uiState
        }
    }

    sealed class UiEvent {
        data class ShowToast(val message: String) : UiEvent()
        object NavigateBack : UiEvent()
    }
}