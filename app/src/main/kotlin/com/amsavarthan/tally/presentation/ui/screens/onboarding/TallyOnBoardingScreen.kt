package com.amsavarthan.tally.presentation.ui.screens.onboarding

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amsavarthan.tally.domain.entity.Account
import com.amsavarthan.tally.domain.entity.AccountType
import com.amsavarthan.tally.domain.entity.TallyOnBoardingStep
import com.amsavarthan.tally.presentation.ui.components.AccountItem
import com.amsavarthan.tally.presentation.ui.components.SummaryItem
import com.amsavarthan.tally.presentation.ui.components.TallyCurrencyTextField
import com.amsavarthan.tally.presentation.ui.screens.NavGraphs
import com.amsavarthan.tally.presentation.ui.screens.destinations.TallyDashboardScreenDestination
import com.amsavarthan.tally.presentation.ui.screens.destinations.TallyManageAccountScreenDestination
import com.amsavarthan.tally.presentation.ui.screens.onboarding.components.TallyScaffold
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import kotlinx.coroutines.flow.collectLatest

@Destination
@Composable
fun TallyOnBoardingScreen(
    navigator: DestinationsNavigator,
    viewModel: OnBoardingScreenViewModel = hiltViewModel(),
) {

    val uiState = viewModel.uiState
    val step = remember(uiState.stepNumber) {
        TallyOnBoardingStep.getStepByNumber(uiState.stepNumber)
    }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is OnBoardingScreenViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                OnBoardingScreenViewModel.UiEvent.NavigateToDashboard -> {
                    navigator.navigate(TallyDashboardScreenDestination) {
                        launchSingleTop = true
                        popUpTo(NavGraphs.root)
                    }
                }
            }
        }
    }

    TallyScaffold(
        step = step,
        onActionClicked = {
            viewModel.onEvent(TallyOnBoardingScreenEvent.OnActionButtonClicked)
        },
    ) {
        when (step) {
            TallyOnBoardingStep.Initial -> Unit
            TallyOnBoardingStep.CashSetup -> OnBoardingCashSetupLayout(
                amount = uiState.cashAmount,
                onAmountChange = { amount ->
                    viewModel.onEvent(TallyOnBoardingScreenEvent.OnCashAmountEntered(amount = amount))
                },
                onDone = {
                    viewModel.onEvent(TallyOnBoardingScreenEvent.OnActionButtonClicked)
                }
            )
            TallyOnBoardingStep.DebitCardSetup -> OnBoardingAccountsSetupLayout(
                accounts = uiState.accounts,
                accountType = AccountType.DebitCard,
                navigator = navigator,
            )
            TallyOnBoardingStep.CreditCardSetup -> OnBoardingAccountsSetupLayout(
                accounts = uiState.accounts,
                accountType = AccountType.CreditCard,
                navigator = navigator,
            )
            TallyOnBoardingStep.PayLaterSetup -> OnBoardingAccountsSetupLayout(
                accounts = uiState.accounts,
                accountType = AccountType.PayLater,
                addButtonLabel = "Add account",
                navigator = navigator,
            )
            TallyOnBoardingStep.Summary -> OnBoardingSummaryLayout(
                outstandingBalance = viewModel.uiState.outstandingBalance,
                repaymentAmount = viewModel.uiState.repaymentAmount,
            )
        }
    }

    BackHandler(enabled = uiState.canGoBack) {
        viewModel.onEvent(TallyOnBoardingScreenEvent.OnBackPressed)
    }

}

@Composable
private fun OnBoardingSummaryLayout(
    outstandingBalance: String,
    repaymentAmount: String,
) {
    Column(
        modifier = Modifier.padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        SummaryItem(
            label = "Outstanding Balance",
            value = outstandingBalance
        )

        SummaryItem(
            label = "Repayment Amount",
            value = repaymentAmount
        )
    }
}


@Composable
private fun OnBoardingCashSetupLayout(
    amount: String,
    onAmountChange: (String) -> Unit,
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
        onValueChange = onAmountChange,
        onImeDone = onDone
    )
}

@Composable
private fun OnBoardingAccountsSetupLayout(
    accounts: List<Account>,
    accountType: AccountType,
    navigator: DestinationsNavigator,
    addButtonLabel: String = "Add card",
) {

    val filteredAccounts = remember(accounts) {
        accounts.filter { account ->
            account.type == accountType
        }
    }

    Column(modifier = Modifier.padding(top = 16.dp)) {
        filteredAccounts.forEach { account ->
            AccountItem(
                name = account.name,
                onClick = {
                    navigator.navigate(
                        TallyManageAccountScreenDestination(
                            accountId = account.id,
                            accountType = accountType,
                        )
                    ) {
                        launchSingleTop = true
                    }
                },
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        AccountItem(
            filled = true,
            name = addButtonLabel,
            onClick = {
                navigator.navigate(
                    TallyManageAccountScreenDestination(
                        accountType = accountType,
                    )
                ) {
                    launchSingleTop = true
                }
            }
        )
    }
}