package com.amsavarthan.tally.presentation.ui.screens.manage_category

import com.amsavarthan.tally.domain.entity.CategoryType

sealed interface TallyManageCategoryScreenEvent {

    data class OnCategoryNameChange(val name: String) : TallyManageCategoryScreenEvent
    data class OnCategoryEmojiChange(val emoji: String) : TallyManageCategoryScreenEvent
    data class OnCategoryTypeChange(val type: CategoryType) : TallyManageCategoryScreenEvent

    data class ToggleDeleteConfirmationAlertDialog(val showDialog: Boolean = false) :
        TallyManageCategoryScreenEvent

    object OnDeleteConfirmation : TallyManageCategoryScreenEvent
    object OnDoneButtonClicked : TallyManageCategoryScreenEvent

}