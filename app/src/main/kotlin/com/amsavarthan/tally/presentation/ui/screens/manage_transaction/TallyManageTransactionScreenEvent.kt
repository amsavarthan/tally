package com.amsavarthan.tally.presentation.ui.screens.manage_transaction

import com.amsavarthan.tally.domain.entity.TallyKeyPadItem
import kotlinx.datetime.LocalDateTime

sealed interface TallyManageTransactionScreenEvent {
    object OnSaveButtonClicked : TallyManageTransactionScreenEvent
    object OnDeleteConfirmation : TallyManageTransactionScreenEvent

    data class ToggleDeleteConfirmationAlertDialog(val showDialog: Boolean = false) :
        TallyManageTransactionScreenEvent

    data class OnDateChanged(val dateTime: LocalDateTime) : TallyManageTransactionScreenEvent
    data class OnKeyPadKeyPressed(val key: TallyKeyPadItem) : TallyManageTransactionScreenEvent
    data class OnKeyPadKeyLongPressed(val key: TallyKeyPadItem) : TallyManageTransactionScreenEvent
}