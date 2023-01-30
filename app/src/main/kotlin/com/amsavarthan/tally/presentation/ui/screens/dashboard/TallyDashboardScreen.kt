package com.amsavarthan.tally.presentation.ui.screens.dashboard

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amsavarthan.tally.R
import com.amsavarthan.tally.domain.utils.DateFormatter
import com.amsavarthan.tally.presentation.ui.components.SummaryItem
import com.amsavarthan.tally.presentation.ui.components.TransactionItem
import com.amsavarthan.tally.presentation.ui.screens.NavGraphs
import com.amsavarthan.tally.presentation.ui.screens.destinations.TallyManageTransactionScreenDestination
import com.amsavarthan.tally.presentation.ui.screens.destinations.TallyOnBoardingScreenDestination
import com.amsavarthan.tally.presentation.ui.screens.destinations.TallyTransactionsScreenDestination
import com.amsavarthan.tally.presentation.ui.screens.destinations.TallyWalletScreenDestination
import com.amsavarthan.tally.presentation.ui.theme.DarkGray
import com.amsavarthan.tally.presentation.ui.theme.fonts
import com.amsavarthan.tally.presentation.utils.ContentDescription
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun TallyDashboardScreen(
    navigator: DestinationsNavigator,
    viewModel: DashboardViewModel = hiltViewModel(),
) {

    val hasUserOnBoarded by viewModel.hasUserOnBoarded.collectAsState()

    LaunchedEffect(hasUserOnBoarded) {
        if (hasUserOnBoarded == false){
            navigator.navigate(TallyOnBoardingScreenDestination) {
                launchSingleTop = true
                popUpTo(NavGraphs.root)
            }
        }
    }

    //Do not draw any content until user has onboarded
    if (hasUserOnBoarded != true) return

    val (
        outstandingBalanceAmount,
        repaymentAmount,
        currentWeekData,
        totalTransactions,
    ) = viewModel.uiState

    val sixthPercentOfScreenHeight = (LocalConfiguration.current.screenHeightDp * 0.6).dp
    val sheetPeekHeight = if (currentWeekData.transactions.isEmpty()) 0.dp else {
        sixthPercentOfScreenHeight
    }
    val fabPosition = when {
        totalTransactions == 0 -> FabPosition.End
        currentWeekData.transactions.isEmpty() -> FabPosition.Center
        else -> FabPosition.End
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                backgroundColor = MaterialTheme.colors.primary,
                onClick = {
                    navigator.navigate(TallyManageTransactionScreenDestination()) {
                        launchSingleTop = true
                    }
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = ContentDescription.buttonAdd
                )
            }
        },
        floatingActionButtonPosition = fabPosition,
        backgroundColor = Color.Black
    ) { padding ->

        BottomSheetScaffold(
            modifier = Modifier
                .padding(padding),
            sheetContent = {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .clickable {
                        navigator.navigate(TallyTransactionsScreenDestination()) {
                            launchSingleTop = true
                        }
                    }
                    .padding(all = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "All Transactions",
                        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Medium)
                    )
                    Icon(
                        imageVector = Icons.Outlined.ChevronRight,
                        contentDescription = ContentDescription.buttonAllTransactions
                    )
                }
                LazyColumn {
                    currentWeekData.transactions.groupBy { it.localDateTime.date }
                        .forEach { (date, transactions) ->
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
            },
            sheetPeekHeight = sheetPeekHeight,
            sheetShape = RectangleShape,
        ) {

            when (totalTransactions) {
                null -> Unit
                0 -> {
                    EmptyTransactionsLayout(
                        outstandingBalanceAmount = outstandingBalanceAmount,
                        repaymentAmount = repaymentAmount,
                        onWalletIconClicked = {
                            navigator.navigate(TallyWalletScreenDestination) {
                                launchSingleTop = true
                            }
                        })
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black)
                            .statusBarsPadding()
                            .padding(horizontal = 24.dp)
                            .padding(top = 16.dp, bottom = 160.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            WalletIcon(color = Color.White, onClick = {
                                navigator.navigate(TallyWalletScreenDestination) {
                                    launchSingleTop = true
                                }
                            })
                            if (currentWeekData.transactions.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(24.dp))
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 24.dp),
                                    verticalArrangement = Arrangement.spacedBy(
                                        8.dp, Alignment.CenterVertically
                                    ),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = "Spent this week", color = Color.White)
                                    Text(
                                        text = "₹${currentWeekData.amountSpent}",
                                        color = Color.White,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 42.sp,
                                        fontFamily = fonts
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.height(32.dp))
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(
                                        16.dp, Alignment.CenterVertically
                                    )
                                ) {
                                    Text(
                                        text = "A fresh new week",
                                        style = MaterialTheme.typography.h5,
                                        textAlign = TextAlign.Center,
                                        color = Color.White,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "Click on the + button to record a\ntransaction on the go",
                                        style = MaterialTheme.typography.subtitle1,
                                        textAlign = TextAlign.Center,
                                        color = Color.White
                                    )
                                    OutlinedButton(colors = ButtonDefaults.outlinedButtonColors(
                                        backgroundColor = Color.Black,
                                        contentColor = Color.White
                                    ), border = BorderStroke(
                                        width = ButtonDefaults.OutlinedBorderSize,
                                        color = MaterialTheme.colors.primary
                                    ), shape = CircleShape, onClick = {
                                        navigator.navigate(TallyTransactionsScreenDestination()) {
                                            launchSingleTop = true
                                        }
                                    }) {
                                        Text(text = "View All Transactions")
                                    }
                                }
                            }
                        }

                        if (currentWeekData.transactions.isEmpty()) {
                            val resourceId by remember {
                                mutableStateOf(
                                    listOf(
                                        R.drawable.illustration_women_feeding_cat,
                                        R.drawable.illustration_women_shopping_run,
                                        R.drawable.illustration_man_coffee_walking
                                    ).random()
                                )
                            }
                            Image(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                painter = painterResource(id = resourceId),
                                contentDescription = null
                            )
                        }
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
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
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
                    label = "Outstanding Balance", value = outstandingBalanceAmount
                )
                SummaryItem(
                    label = "Repayment Amount", value = repaymentAmount
                )
                Text(text = "You haven’t made any transactions yet.", color = DarkGray)
            }
        }
        Image(
            modifier = Modifier.padding(start = 32.dp, bottom = 16.dp),
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
            modifier = Modifier.align(Alignment.CenterEnd), onClick = onClick
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