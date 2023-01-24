package com.amsavarthan.tally.presentation.ui.screens.manage_account


sealed interface TallyManageAccountScreenEvent {

    object OnActionButtonClicked : TallyManageAccountScreenEvent
    object OnDeleteClicked : TallyManageAccountScreenEvent

    data class ToggleUnsavedChangesAlertDialog(val visible: Boolean = false) :
        TallyManageAccountScreenEvent

    data class ToggleDeleteConfirmationAlertDialog(val visible: Boolean = false) :
        TallyManageAccountScreenEvent

    data class OnAccountNameEntered(val name: String) : TallyManageAccountScreenEvent
    data class OnAccountBalanceEntered(val balance: String) : TallyManageAccountScreenEvent
    data class OnAccountLimitEntered(val limit: String) : TallyManageAccountScreenEvent


}