package com.amsavarthan.tally.presentation.ui.screens.accounts

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.amsavarthan.tally.domain.entity.AccountType
import com.amsavarthan.tally.domain.usecase.GetAccountsByTypeUseCase
import com.amsavarthan.tally.presentation.ui.screens.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AccountsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getAccountsByTypeUseCase: GetAccountsByTypeUseCase,
) : ViewModel() {

    private val navArgs = savedStateHandle.navArgs<TallyAccountsScreenNavArgs>()
    val accountType: AccountType = navArgs.accountType

    @OptIn(SavedStateHandleSaveableApi::class)
    var uiState by savedStateHandle.saveable { mutableStateOf(TallyAccountsScreenState()) }
        private set

    init {
        viewModelScope.launch {
            getAccountsByTypeUseCase(accountType).collectLatest { accounts ->
                uiState = uiState.copy(accounts = accounts)
            }
        }
    }

}