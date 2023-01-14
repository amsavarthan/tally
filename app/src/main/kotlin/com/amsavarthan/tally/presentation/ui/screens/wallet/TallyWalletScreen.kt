package com.amsavarthan.tally.presentation.ui.screens.wallet

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amsavarthan.tally.domain.entity.AccountType
import com.amsavarthan.tally.presentation.ui.components.AccountItem
import com.amsavarthan.tally.presentation.ui.components.SummaryItem
import com.amsavarthan.tally.presentation.ui.components.TallyAppBar
import com.amsavarthan.tally.presentation.ui.screens.destinations.TallyManageAccountScreenDestination
import com.amsavarthan.tally.presentation.ui.screens.destinations.TallyAccountsScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.text.DecimalFormat

@Destination
@Composable
fun TallyWalletScreen(
    navigator: DestinationsNavigator,
    viewModel: WalletViewModel = hiltViewModel(),
) {

    val (walletAmountDetails) = viewModel.uiState

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            TallyAppBar(
                elevation = 0.dp,
                navigationIcon = {
                    IconButton(onClick = navigator::navigateUp) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "button_back",
                        )
                    }
                },
                title = "Wallet",
                actions = {
                    //This is required for UI spacing purpose
                    Spacer(modifier = Modifier.width(64.dp))
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp, top = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                val formatter = remember {
                    DecimalFormat("0.00")
                }
                SummaryItem(
                    label = "Cash Holdings",
                    value = formatter.format(walletAmountDetails.cashHoldings)
                )
                SummaryItem(
                    label = "Outstanding Balance",
                    value = formatter.format(walletAmountDetails.outstandingBalance)
                )
                SummaryItem(
                    label = "Repayment Amount",
                    value = formatter.format(walletAmountDetails.repaymentAmount)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AccountType.getAllAccountTypes().forEach { accountType ->
                    val suffixForPlural = if (accountType == AccountType.Cash) "" else "s"
                    AccountItem(
                        name = "${accountType.title}$suffixForPlural",
                        filled = true,
                        trailingIcon = Icons.Outlined.ChevronRight,
                        onClick = {
                            if (accountType == AccountType.Cash) {
                                navigator.navigate(
                                    TallyManageAccountScreenDestination(
                                        accountType = accountType,
                                    )
                                ) {
                                    launchSingleTop = true
                                }
                                return@AccountItem
                            }

                            navigator.navigate(
                                TallyAccountsScreenDestination(
                                    accountType = accountType,
                                )
                            ) {
                                launchSingleTop = true
                            }
                        },
                    )
                }
            }
        }
    }

}