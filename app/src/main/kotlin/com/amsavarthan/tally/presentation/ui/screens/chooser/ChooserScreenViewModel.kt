package com.amsavarthan.tally.presentation.ui.screens.chooser

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.amsavarthan.tally.domain.entity.AccountType
import com.amsavarthan.tally.domain.entity.ChooserType
import com.amsavarthan.tally.domain.repository.AccountsRepository
import com.amsavarthan.tally.domain.repository.AppDataRepository
import com.amsavarthan.tally.domain.repository.CategoryRepository
import com.amsavarthan.tally.presentation.ui.screens.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooserScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    accountsRepository: AccountsRepository,
    categoryRepository: CategoryRepository,
    private val appDataRepository: AppDataRepository,
) : ViewModel() {

    val navArgs: TallyChooserScreenNavArgs = savedStateHandle.navArgs()

    @OptIn(SavedStateHandleSaveableApi::class)
    var uiState by savedStateHandle.saveable { mutableStateOf(TallyChooserScreenState()) }
        private set

    init {
        viewModelScope.launch {
            when (navArgs.type) {
                ChooserType.Account -> {
                    accountsRepository.getAccounts().collectLatest { accounts ->
                        uiState = uiState.copy(
                            accounts = accounts,
                            cashAccount = accounts.firstOrNull { it.type == AccountType.Cash }
                        )
                    }
                }
                ChooserType.Category -> {
                    categoryRepository.getCategories().collectLatest { categories ->
                        uiState = uiState.copy(categories = categories)
                    }
                }
            }
        }
    }

    fun updateAppPreferenceWith(categoryId: Long?) {
        viewModelScope.launch {
            appDataRepository.setLastSelectedCategoryId(categoryId ?: return@launch)
        }
    }

}