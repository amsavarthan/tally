package com.amsavarthan.tally.presentation.ui.screens.manage_transaction

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.amsavarthan.tally.domain.entity.TallyKeyPadItem
import com.amsavarthan.tally.domain.entity.toTransaction
import com.amsavarthan.tally.domain.repository.AccountsRepository
import com.amsavarthan.tally.domain.repository.CategoryRepository
import com.amsavarthan.tally.domain.repository.TransactionRepository
import com.amsavarthan.tally.domain.usecase.AddOrUpdateTransactionUseCase
import com.amsavarthan.tally.domain.usecase.GetTransactionDetailsUseCase
import com.amsavarthan.tally.domain.utils.MAX_AMOUNT_LIMIT
import com.amsavarthan.tally.domain.utils.toCurrencyInt
import com.amsavarthan.tally.presentation.ui.screens.chooser.TallyChooserResult
import com.amsavarthan.tally.presentation.ui.screens.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ManageTransactionScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val accountsRepository: AccountsRepository,
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository,
    private val addOrUpdateTransactionUseCase: AddOrUpdateTransactionUseCase,
    getTransactionDetailsUseCase: GetTransactionDetailsUseCase,
) : ViewModel() {

    private val navArgs: TallyManageTransactionScreenNavArgs = savedStateHandle.navArgs()

    @OptIn(SavedStateHandleSaveableApi::class)
    var uiState by savedStateHandle.saveable { mutableStateOf(TallyManageTransactionScreenState()) }
        private set

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            uiState = uiState.copy(
                transactionDetail = getTransactionDetailsUseCase(navArgs.transactionId)
            )
        }
    }

    fun onEvent(event: TallyManageTransactionScreenEvent) {
        when (event) {
            is TallyManageTransactionScreenEvent.OnKeyPadKeyPressed -> {
                when (event.key) {
                    TallyKeyPadItem.Key0 -> insertKey("0")
                    TallyKeyPadItem.Key1 -> insertKey("1")
                    TallyKeyPadItem.Key2 -> insertKey("2")
                    TallyKeyPadItem.Key3 -> insertKey("3")
                    TallyKeyPadItem.Key4 -> insertKey("4")
                    TallyKeyPadItem.Key5 -> insertKey("5")
                    TallyKeyPadItem.Key6 -> insertKey("6")
                    TallyKeyPadItem.Key7 -> insertKey("7")
                    TallyKeyPadItem.Key8 -> insertKey("8")
                    TallyKeyPadItem.Key9 -> insertKey("9")
                    TallyKeyPadItem.KeyDot -> insertKey(".")
                    TallyKeyPadItem.KeyBackspace -> removeKey()
                }
            }
            is TallyManageTransactionScreenEvent.OnKeyPadKeyLongPressed -> {
                if (event.key != TallyKeyPadItem.KeyBackspace) return
                val updatedDetails = uiState.transactionDetail.copy(amount = "")
                uiState = uiState.copy(transactionDetail = updatedDetails)
            }
            is TallyManageTransactionScreenEvent.OnDateChanged -> {
                val updatedDetails = uiState.transactionDetail.copy(localDateTime = event.dateTime)
                uiState = uiState.copy(transactionDetail = updatedDetails)
            }
            is TallyManageTransactionScreenEvent.ToggleDeleteConfirmationAlertDialog -> {
                uiState = uiState.copy(shouldShowDeleteConfirmationAlertDialog = event.showDialog)
            }
            TallyManageTransactionScreenEvent.OnSaveButtonClicked -> {
                viewModelScope.launch {
                    val (isSuccess, reason) = addOrUpdateTransactionUseCase(transactionDetails = uiState.transactionDetail)

                    if (isSuccess) {
                        _events.emit(UiEvent.NavigateBack)
                        return@launch
                    }

                    _events.emit(UiEvent.ShowToast(message = reason))
                }
            }
            TallyManageTransactionScreenEvent.OnDeleteConfirmation -> {
                viewModelScope.launch {
                    uiState = uiState.copy(shouldShowDeleteConfirmationAlertDialog = false)
                    transactionRepository.deleteTransaction(uiState.transactionDetail.toTransaction())
                    _events.emit(UiEvent.NavigateBack)
                }
            }
        }
    }

    private fun removeKey() {
        val updatedDetails =
            uiState.transactionDetail.copy(amount = uiState.transactionDetail.amount.dropLast(1))
        uiState = uiState.copy(transactionDetail = updatedDetails)
    }

    private fun insertKey(key: String) {

        val amount = "${uiState.transactionDetail.amount}$key"

        if ((amount.toCurrencyInt() ?: return) > MAX_AMOUNT_LIMIT) {
            viewModelScope.launch {
                _events.emit(UiEvent.ShowToast("Value cannot be more than ₹1,00,00,000"))
            }
            return
        }

        val updatedDetails = uiState.transactionDetail.copy(amount = amount)
        uiState = uiState.copy(transactionDetail = updatedDetails)
    }

    fun updateChosenAccountAndCategory(result: TallyChooserResult) {
        viewModelScope.launch {
            val updatedDetails = when {
                result.accountId != null -> {
                    val account = accountsRepository.getAccount(result.accountId)
                    uiState.transactionDetail.copy(account = account)
                }
                result.categoryId != null -> {
                    val category = categoryRepository.getCategory(result.categoryId)
                    uiState.transactionDetail.copy(category = category)
                }
                else -> uiState.transactionDetail
            }
            uiState = uiState.copy(transactionDetail = updatedDetails)
        }
    }

    sealed interface UiEvent {
        data class ShowToast(val message: String) : UiEvent
        object NavigateBack : UiEvent
    }

}