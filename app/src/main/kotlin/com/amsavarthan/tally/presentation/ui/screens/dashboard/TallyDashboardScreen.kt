package com.amsavarthan.tally.presentation.ui.screens.dashboard

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amsavarthan.tally.R
import com.amsavarthan.tally.domain.entity.AccountType
import com.amsavarthan.tally.domain.entity.CategoryType
import com.amsavarthan.tally.domain.entity.TransactionDetail
import com.amsavarthan.tally.domain.utils.DateFormatter
import com.amsavarthan.tally.presentation.ui.components.SummaryItem
import com.amsavarthan.tally.presentation.ui.screens.NavGraphs
import com.amsavarthan.tally.presentation.ui.screens.destinations.TallyManageTransactionScreenDestination
import com.amsavarthan.tally.presentation.ui.screens.destinations.TallyOnBoardingScreenDestination
import com.amsavarthan.tally.presentation.ui.screens.destinations.TallyWalletScreenDestination
import com.amsavarthan.tally.presentation.ui.theme.DarkGray
import com.amsavarthan.tally.presentation.ui.theme.LightGray
import com.amsavarthan.tally.presentation.ui.theme.fonts
import com.amsavarthan.tally.presentation.utils.ContentDescription
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
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = ContentDescription.buttonAdd
                )
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
                                    text = "₹$spentThisWeek",
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 42.sp,
                                    fontFamily = fonts
                                )
                            }
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .clickable { }
                                .padding(all = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Last Transactions",
                                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Medium)
                            )
                            Icon(
                                imageVector = Icons.Outlined.ChevronRight,
                                contentDescription = ContentDescription.buttonAllTransactions
                            )
                        }
                    }
                    transactions.groupBy { it.localDateTime.date }.forEach { (date, transactions) ->
                        stickyHeader {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                text = DateFormatter(date),
                                color = DarkGray,
                                style = MaterialTheme.typography.subtitle2
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                        items(transactions) { transaction ->
                            TransactionItem(
                                transaction = transaction,
                                onLongClick = {
                                    navigator.navigate(
                                        TallyManageTransactionScreenDestination(
                                            transactionId = transaction.transactionId
                                        )
                                    ) {
                                        launchSingleTop = true
                                    }
                                },
                                onClick = {},
                            )
                        }
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionItem(
    transaction: TransactionDetail,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    requireNotNull(transaction.category)
    requireNotNull(transaction.account)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onLongClick = onLongClick, onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text(text = transaction.category.emoji, fontSize = 16.sp)
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = transaction.category.name,
                fontFamily = fonts,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            val creditOrDebit = remember {
                when (transaction.account.type) {
                    AccountType.Cash -> {
                        when (transaction.category.type) {
                            CategoryType.Expense -> "Paid as"
                            CategoryType.Income -> "Received as"
                        }
                    }
                    else -> {
                        when (transaction.category.type) {
                            CategoryType.Expense -> "Debited from"
                            CategoryType.Income -> "Credited to"
                        }
                    }
                }

            }
            Text(
                text = "$creditOrDebit ${transaction.account.name}",
                fontFamily = fonts,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = DarkGray
            )
        }
        Text(
            text = "₹${transaction.amount}",
            color = if (transaction.category.type == CategoryType.Income) MaterialTheme.colors.primary else Color.Black,
            fontFamily = fonts,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
        )
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
                contentDescription = ContentDescription.buttonWallet,
                tint = color
            )
        }
    }
}