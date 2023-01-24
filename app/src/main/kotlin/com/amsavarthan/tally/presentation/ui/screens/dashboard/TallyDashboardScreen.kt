package com.amsavarthan.tally.presentation.ui.screens.dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amsavarthan.tally.R
import com.amsavarthan.tally.domain.utils.MAX_AMOUNT_LIMIT
import com.amsavarthan.tally.presentation.ui.components.SummaryItem
import com.amsavarthan.tally.presentation.ui.screens.NavGraphs
import com.amsavarthan.tally.presentation.ui.screens.destinations.TallyManageTransactionScreenDestination
import com.amsavarthan.tally.presentation.ui.screens.destinations.TallyOnBoardingScreenDestination
import com.amsavarthan.tally.presentation.ui.screens.destinations.TallyWalletScreenDestination
import com.amsavarthan.tally.presentation.ui.theme.DarkGray
import com.amsavarthan.tally.presentation.ui.theme.fonts
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo

@OptIn(ExperimentalFoundationApi::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun DashboardScreen(
    navigator: DestinationsNavigator,
    viewModel: DashboardViewModel = hiltViewModel(),
) {

    val hasUserOnBoarded by viewModel.hasUserOnBoarded.collectAsState()

    LaunchedEffect(hasUserOnBoarded) {
        if (hasUserOnBoarded == false) {
            navigator.navigate(TallyOnBoardingScreenDestination) {
                launchSingleTop = true
                popUpTo(NavGraphs.root)
            }
        }
    }

    //Do not draw any content until user has onboarded
    if (hasUserOnBoarded == null || hasUserOnBoarded == false) return

    val (
        outstandingBalanceAmount,
        repaymentAmount,
        transactions,
        spentThisWeek,
    ) = viewModel.uiState

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(16.dp),
                onClick = {
                    navigator.navigate(TallyManageTransactionScreenDestination()) {
                        launchSingleTop = true
                    }
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "button_add")
            }
        },
    ) { padding ->
        when {
            transactions.isEmpty() -> {
                EmptyTransactionsLayout(
                    outstandingBalanceAmount = outstandingBalanceAmount,
                    repaymentAmount = repaymentAmount,
                    onWalletIconClicked = {
                        navigator.navigate(TallyWalletScreenDestination) {
                            launchSingleTop = true
                        }
                    }
                )
            }
            else -> {
                LazyColumn {
                    item {
                        Column(
                            modifier = Modifier
                                .background(Color.Black)
                                .padding(horizontal = 24.dp)
                                .padding(top = 16.dp),
                        ) {
                            WalletIcon(color = Color.White, onClick = {
                                navigator.navigate(TallyWalletScreenDestination) {
                                    launchSingleTop = true
                                }
                            })
                            Spacer(modifier = Modifier.height(24.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 24.dp),
                                verticalArrangement = Arrangement.spacedBy(
                                    8.dp,
                                    Alignment.CenterVertically
                                ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "Spent this week", color = Color.White)
                                Text(
                                    text = "₹$MAX_AMOUNT_LIMIT",
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 42.sp,
                                    fontFamily = fonts
                                )
                            }
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                    stickyHeader {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .clickable { }
                                .padding(all = 24.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Last Transactions",
                                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Medium)
                            )
                            Icon(
                                imageVector = Icons.Outlined.ChevronRight,
                                contentDescription = "button_all_transactions"
                            )
                        }
                    }
                    transactions.groupBy { it.dateTime }.forEach { date, transactions ->
                        item {  }
                    }
                }
            }
        }
    }

}

@Composable
fun EmptyTransactionsLayout(
    outstandingBalanceAmount: String,
    repaymentAmount: String,
    onWalletIconClicked: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp),
        ) {
            WalletIcon(color = Color.Black, onClick = onWalletIconClicked)
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                SummaryItem(
                    label = "Outstanding Balance",
                    value = outstandingBalanceAmount
                )
                SummaryItem(
                    label = "Repayment Amount",
                    value = repaymentAmount
                )
                Text(text = "You haven’t made any transactions yet.", color = DarkGray)
            }
        }
        Image(
            modifier = Modifier.padding(32.dp),
            painter = painterResource(id = R.drawable.illustration_man_checks_phone),
            contentDescription = null,
            alignment = Alignment.BottomStart
        )
    }
}

@Composable
private fun WalletIcon(
    color: Color,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(56.dp)
    ) {
        IconButton(
            modifier = Modifier.align(Alignment.CenterEnd),
            onClick = onClick
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Outlined.AccountBalanceWallet,
                contentDescription = "button_wallet",
                tint = color
            )
        }
    }
}