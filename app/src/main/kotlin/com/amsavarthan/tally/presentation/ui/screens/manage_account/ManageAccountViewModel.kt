package com.amsavarthan.tally.presentation.ui.screens.manage_account

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.amsavarthan.tally.domain.entity.AccountType
import com.amsavarthan.tally.domain.repository.AccountsRepository
import com.amsavarthan.tally.domain.usecase.AddOrUpdateAccountUseCase
import com.amsavarthan.tally.domain.usecase.UpdateCashAccountBalanceUseCase
import com.amsavarthan.tally.domain.utils.CurrencyFormatter
import com.amsavarthan.tally.domain.utils.MAX_AMOUNT_LIMIT
import com.amsavarthan.tally.domain.utils.toCurrencyInt
import com.amsavarthan.tally.presentation.ui.screens.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageAccountViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val addOrUpdateAccountUseCase: AddOrUpdateAccountUseCase,
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

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            if (accountType != AccountType.Cash) return@launch
            val cashAccount = accountsRepository.getCashAccount().first() ?: return@launch
            uiState = uiState.copy(
                accountId = cashAccount.id,
                cashAmount = CurrencyFormatter(cashAccount.balance)
            )
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
                        outstandingBalance = CurrencyFormatter(account.balance),
                    )
                )
                AccountType.CreditCard -> uiState.copy(
                    creditCardDetails = CreditDetails(
                        name = account.name,
                        outstandingAmount = CurrencyFormatter(account.balance),
                        creditLimit = CurrencyFormatter(account.limit)
                    )
                )
                AccountType.PayLater -> uiState.copy(
                    payLaterAccountDetails = CreditDetails(
                        name = account.name,
                        outstandingAmount = CurrencyFormatter(account.balance),
                        creditLimit = CurrencyFormatter(account.limit)
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
                else -> return@launch
            }.copy(id = uiState.accountId)

            val (accountId, result) = addOrUpdateAccountUseCase(account)
            val (isSuccess, reason) = result

            if (!isSuccess) {
                _events.emit(UiEvent.ShowToast(reason))
                return@launch
            }

            uiState = uiState.copy(accountId = accountId, hasDataChanged = false)
            _events.emit(UiEvent.NavigateBack)
        }
    }

    private fun deleteAccount() {
        viewModelScope.launch {
            uiState = uiState.copy(shouldShowDeleteConfirmationAlert = false)
            val account = accountsRepository.getAccount(uiState.accountId!!)
            accountsRepository.deleteAccount(account)
            _events.emit(UiEvent.NavigateBack)
        }
    }

    private fun updateAccountBalanceAmount(balance: String) {
        viewModelScope.launch {
            val balanceAsInt = balance.toCurrencyInt() ?: return@launch
            if (balanceAsInt > MAX_AMOUNT_LIMIT) {
                viewModelScope.launch {
                    _events.emit(UiEvent.ShowToast("Value cannot be more than ₹1,00,00,000"))
                }
                return@launch
            }

            uiState = uiState.copy(hasDataChanged = accountType != AccountType.Cash)
            when (accountType) {
                AccountType.Cash -> {
                    uiState = uiState.copy(cashAmount = balance)
                    updateCashAccountBalanceUseCase(uiState.accountId, balanceAsInt)
                }
                AccountType.CreditCard -> {
                    uiState = uiState.copy(
                        creditCardDetails = uiState.creditCardDetails.copy(
                            outstandingAmount = balance,
                        )
                    )
                }
                AccountType.DebitCard -> {
                    uiState = uiState.copy(
                        debitCardDetails = uiState.debitCardDetails.copy(
                            outstandingBalance = balance,
                        )
                    )
                }
                AccountType.PayLater -> {
                    uiState = uiState.copy(
                        payLaterAccountDetails = uiState.payLaterAccountDetails.copy(
                            outstandingAmount = balance,
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
        if ((limit.toCurrencyInt() ?: return) > MAX_AMOUNT_LIMIT) {
            viewModelScope.launch {
                _events.emit(UiEvent.ShowToast("Value cannot be more than ₹1,00,00,000"))
            }
            return
        }

        uiState = when (accountType) {
            AccountType.CreditCard -> uiState.copy(
                hasDataChanged = true,
                creditCardDetails = uiState.creditCardDetails.copy(
                    creditLimit = limit,
                )
            )
            AccountType.PayLater -> uiState.copy(
                hasDataChanged = true,
                payLaterAccountDetails = uiState.payLaterAccountDetails.copy(
                    creditLimit = limit,
                )
            )
            AccountType.DebitCard, AccountType.Cash -> uiState
        }
    }

    sealed interface UiEvent {
        data class ShowToast(val message: String) : UiEvent
        object NavigateBack : UiEvent
    }
}