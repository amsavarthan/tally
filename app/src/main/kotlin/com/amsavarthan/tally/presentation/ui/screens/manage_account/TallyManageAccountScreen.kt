package com.amsavarthan.tally.presentation.ui.screens.manage_account

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.twotone.AccountBalanceWallet
import androidx.compose.material.icons.twotone.CreditCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amsavarthan.tally.domain.entity.AccountType
import com.amsavarthan.tally.presentation.ui.components.TallyAppBar
import com.amsavarthan.tally.presentation.ui.components.TallyCurrencyTextField
import com.amsavarthan.tally.presentation.ui.components.TallyTextField
import com.amsavarthan.tally.presentation.ui.screens.onboarding.components.TallyScaffold
import com.amsavarthan.tally.presentation.utils.ContentDescription
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

data class TallyManageAccountScreenNavArgs(
    val accountId: Long? = null,
    val accountType: AccountType,
)

@Destination(navArgsDelegate = TallyManageAccountScreenNavArgs::class)
@Composable
fun TallyManageAccountScreen(
    navigator: DestinationsNavigator,
    viewModel: ManageAccountViewModel = hiltViewModel(),
) {

    val (
        accountId,
        debitCardDetails,
        creditCardDetails,
        payLaterAccountDetails,
        cashAmount,
        hasDataChanged,
        shouldShowUnsavedChangesAlertDialog,
        shouldShowDeleteConfirmationAlertDialog,
    ) = viewModel.uiState

    val accountType = viewModel.accountType

    val icon = remember(accountType) {
        when (accountType) {
            AccountType.DebitCard, AccountType.CreditCard -> Icons.TwoTone.CreditCard
            AccountType.PayLater, AccountType.Cash -> Icons.TwoTone.AccountBalanceWallet
        }
    }
    val action = remember(accountId) {
        if (accountType == AccountType.Cash) return@remember ""
        if (accountId == null) "New " else "Edit "
    }
    val actionText = remember(accountType, accountId) {
        val prefix = if (accountId != null) "Save" else "Add"
        when (accountType) {
            AccountType.DebitCard, AccountType.CreditCard -> "$prefix Card"
            AccountType.PayLater -> "$prefix Account"
            AccountType.Cash -> null
        }
    }


    val description = remember(accountType) {
        if (accountType == AccountType.Cash) "Enter the amount of cash you currently have in your wallet." else null
    }

    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val elevation by animateDpAsState(targetValue = if (scrollState.value > 2) AppBarDefaults.TopAppBarElevation else 0.dp)


    LaunchedEffect(key1 = true) {
        viewModel.events.collectLatest { event ->
            when (event) {
                ManageAccountViewModel.UiEvent.NavigateBack -> {
                    navigator.navigateUp()
                }
                is ManageAccountViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    if (shouldShowUnsavedChangesAlertDialog) {
        AlertDialog(
            title = { Text(text = "Do you want to leave?") },
            text = { Text(text = "Hey there! It looks like you have made some changes but haven't saved them yet.") },
            onDismissRequest = {
                viewModel.onEvent(TallyManageAccountScreenEvent.ToggleUnsavedChangesAlertDialog())
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onEvent(TallyManageAccountScreenEvent.ToggleUnsavedChangesAlertDialog())
                }) {
                    Text(text = "Stay")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.onEvent(TallyManageAccountScreenEvent.ToggleUnsavedChangesAlertDialog())
                    navigator.navigateUp()
                }) {
                    Text(text = "Leave")
                }
            }
        )
    }

    if (shouldShowDeleteConfirmationAlertDialog) {
        AlertDialog(
            title = { Text(text = "Remove ${accountType.title}") },
            text = { Text(text = "Are you sure do you want to remove this ${accountType.title.lowercase()}?") },
            onDismissRequest = {
                viewModel.onEvent(TallyManageAccountScreenEvent.ToggleDeleteConfirmationAlertDialog())
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onEvent(TallyManageAccountScreenEvent.OnDeleteClicked)
                }) {
                    Text(text = "Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.onEvent(TallyManageAccountScreenEvent.ToggleDeleteConfirmationAlertDialog())
                }) {
                    Text(text = "Keep")
                }
            }
        )
    }

    TallyScaffold(
        icon = icon,
        title = "$action${accountType.title}",
        description = description,
        scrollState = scrollState,
        topBar = {
            TallyAppBar(
                elevation = elevation,
                navigationIcon = {
                    IconButton(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        onClick = {
                            if (!hasDataChanged) {
                                navigator.navigateUp()
                                return@IconButton
                            }
                            viewModel.onEvent(
                                TallyManageAccountScreenEvent.ToggleUnsavedChangesAlertDialog(
                                    visible = true
                                )
                            )
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = ContentDescription.buttonBack,
                            tint = MaterialTheme.colors.onBackground
                        )
                    }
                },
                actions = {
                    if (accountType == AccountType.Cash || accountId == null) return@TallyAppBar
                    IconButton(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        onClick = {
                            viewModel.onEvent(
                                TallyManageAccountScreenEvent.ToggleDeleteConfirmationAlertDialog(
                                    visible = true
                                )
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = ContentDescription.buttonDelete,
                            tint = MaterialTheme.colors.onBackground
                        )
                    }
                }
            )
        },
        actionText = actionText,
        onActionClicked = {
            viewModel.onEvent(TallyManageAccountScreenEvent.OnActionButtonClicked)
        }
    ) {
        when (accountType) {
            AccountType.Cash -> EditCashAccountLayout(
                amount = cashAmount,
                onAmountChanged = { amount ->
                    viewModel.onEvent(
                        TallyManageAccountScreenEvent.OnAccountBalanceEntered(balance = amount)
                    )
                },
                onDone = {
                    viewModel.onEvent(TallyManageAccountScreenEvent.OnActionButtonClicked)
                }
            )
            AccountType.DebitCard -> AddEditDebitCardLayout(
                details = debitCardDetails,
                onNameChanged = { name ->
                    viewModel.onEvent(
                        TallyManageAccountScreenEvent.OnAccountNameEntered(name = name)
                    )
                },
                onAmountChanged = { amount ->
                    viewModel.onEvent(
                        TallyManageAccountScreenEvent.OnAccountBalanceEntered(balance = amount)
                    )
                }
            )
            AccountType.CreditCard -> AddEditCreditCardLayout(
                details = creditCardDetails,
                onNameChanged = { name ->
                    viewModel.onEvent(
                        TallyManageAccountScreenEvent.OnAccountNameEntered(name = name)
                    )
                },
                onAmountChanged = { amount ->
                    viewModel.onEvent(
                        TallyManageAccountScreenEvent.OnAccountBalanceEntered(balance = amount)
                    )
                },
                onCreditLimitAmountChanged = { limit ->
                    viewModel.onEvent(
                        TallyManageAccountScreenEvent.OnAccountLimitEntered(limit = limit)
                    )
                }
            )
            AccountType.PayLater -> AddEditPayLaterAccountLayout(
                details = payLaterAccountDetails,
                onNameChanged = { name ->
                    viewModel.onEvent(
                        TallyManageAccountScreenEvent.OnAccountNameEntered(name = name)
                    )
                },
                onAmountChanged = { amount ->
                    viewModel.onEvent(
                        TallyManageAccountScreenEvent.OnAccountBalanceEntered(balance = amount)
                    )
                },
                onCreditLimitAmountChanged = { limit ->
                    viewModel.onEvent(
                        TallyManageAccountScreenEvent.OnAccountLimitEntered(limit = limit)
                    )
                }
            )
        }
    }

    BackHandler {
        if (!hasDataChanged) {
            navigator.navigateUp()
            return@BackHandler
        }
        viewModel.onEvent(
            TallyManageAccountScreenEvent.ToggleUnsavedChangesAlertDialog(
                visible = true
            )
        )
    }

}


@Composable
private fun EditCashAccountLayout(
    amount: String,
    onAmountChanged: (String) -> Unit,
    onDone: () -> Unit,
) {
    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    TallyCurrencyTextField(
        modifier = Modifier.focusRequester(focusRequester),
        value = amount,
        onValueChange = onAmountChanged,
        onImeDone = onDone
    )
}

@Composable
private fun AddEditDebitCardLayout(
    details: DebitCardDetails,
    onNameChanged: (String) -> Unit,
    onAmountChanged: (String) -> Unit,
) {
    Column(
        modifier = Modifier.padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        TallyTextField(
            label = "Account Name",
            placeholder = "Salary Account",
            value = details.name,
            onValueChange = onNameChanged
        )

        TallyCurrencyTextField(
            label = "Outstanding Balance",
            value = details.outstandingBalance,
            onValueChange = onAmountChanged
        )
    }
}

@Composable
private fun AddEditCreditCardLayout(
    details: CreditDetails,
    onNameChanged: (String) -> Unit,
    onAmountChanged: (String) -> Unit,
    onCreditLimitAmountChanged: (String) -> Unit,
) {
    Column(
        modifier = Modifier.padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        TallyTextField(
            label = "Account Name",
            placeholder = "HDFC",
            value = details.name,
            onValueChange = onNameChanged
        )

        TallyCurrencyTextField(
            label = "Credit Limit",
            value = details.creditLimit,
            onValueChange = onCreditLimitAmountChanged
        )

        TallyCurrencyTextField(
            label = "Outstanding Amount",
            value = details.outstandingAmount,
            onValueChange = onAmountChanged
        )
    }
}

@Composable
private fun AddEditPayLaterAccountLayout(
    details: CreditDetails,
    onNameChanged: (String) -> Unit,
    onAmountChanged: (String) -> Unit,
    onCreditLimitAmountChanged: (String) -> Unit,
) {
    Column(
        modifier = Modifier.padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TallyTextField(
            label = "Account Name",
            placeholder = "Simpl",
            value = details.name,
            onValueChange = onNameChanged
        )

        TallyCurrencyTextField(
            label = "Credit Limit",
            value = details.creditLimit,
            onValueChange = onCreditLimitAmountChanged
        )

        TallyCurrencyTextField(
            label = "Outstanding Amount",
            value = details.outstandingAmount,
            onValueChange = onAmountChanged
        )
    }
}