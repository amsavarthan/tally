package com.amsavarthan.tally.presentation.ui.screens.manage_category

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.amsavarthan.tally.domain.repository.CategoryRepository
import com.amsavarthan.tally.domain.usecase.AddOrUpdateCategoryUseCase
import com.amsavarthan.tally.presentation.ui.screens.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageCategoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val categoryRepository: CategoryRepository,
    private val addOrUpdateCategoryUseCase: AddOrUpdateCategoryUseCase,
) : ViewModel() {

    private val navArgs: TallyManageCategoryScreenNavArgs = savedStateHandle.navArgs()

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    @OptIn(SavedStateHandleSaveableApi::class)
    var uiState by savedStateHandle.saveable {
        mutableStateOf(
            TallyManageCategoryScreenState.with(
                categoryType = navArgs.categoryType
            )
        )
    }
        private set

    init {
        viewModelScope.launch {
            val category = categoryRepository.getCategory(navArgs.categoryId ?: return@launch)
            uiState = uiState.copy(category = category)
        }
    }

    fun onEvent(event: TallyManageCategoryScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is TallyManageCategoryScreenEvent.OnCategoryEmojiChange -> {
                    val updatedCategory = uiState.category.copy(emoji = event.emoji)
                    uiState = uiState.copy(category = updatedCategory)
                }
                is TallyManageCategoryScreenEvent.OnCategoryNameChange -> {
                    val updatedCategory = uiState.category.copy(name = event.name)
                    uiState = uiState.copy(category = updatedCategory)
                }
                is TallyManageCategoryScreenEvent.OnCategoryTypeChange -> {
                    val updatedCategory = uiState.category.copy(type = event.type)
                    uiState = uiState.copy(category = updatedCategory)
                }
                is TallyManageCategoryScreenEvent.ToggleDeleteConfirmationAlertDialog -> {
                    uiState = uiState.copy(shouldShowDeleteConfirmationDialog = event.showDialog)
                }
                TallyManageCategoryScreenEvent.OnDeleteConfirmation -> {
                    uiState = uiState.copy(shouldShowDeleteConfirmationDialog = false)
                    categoryRepository.deleteCategory(uiState.category)
                    _events.emit(UiEvent.NavigateBack)
                }
                TallyManageCategoryScreenEvent.OnDoneButtonClicked -> {
                    val isSuccess = addOrUpdateCategoryUseCase(uiState.category)
                    if (isSuccess) {
                        _events.emit(UiEvent.NavigateBack)
                        return@launch
                    }
                    _events.emit(UiEvent.ShowSnackBar("Invalid details provided"))
                }
            }
        }
    }

    sealed interface UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent
        object NavigateBack : UiEvent
    }

}