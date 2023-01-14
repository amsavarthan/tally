package com.amsavarthan.tally.presentation.ui.screens.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amsavarthan.tally.R
import com.amsavarthan.tally.presentation.ui.components.SummaryItem
import com.amsavarthan.tally.presentation.ui.screens.NavGraphs
import com.amsavarthan.tally.presentation.ui.screens.destinations.TallyOnBoardingScreenDestination
import com.amsavarthan.tally.presentation.ui.screens.destinations.TallyWalletScreenDestination
import com.amsavarthan.tally.presentation.ui.theme.DarkGray
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import java.text.DecimalFormat

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
    ) = viewModel.uiState

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(16.dp),
                onClick = { /*TODO*/ },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "button_add")
            }
        },
    ) { padding ->
        if (transactions.isEmpty()) {
            EmptyTransactionsLayout(
                outstandingBalanceAmount = outstandingBalanceAmount,
                repaymentAmount = repaymentAmount,
                navigateToWalletScreen = {
                    navigator.navigate(TallyWalletScreenDestination)
                }
            )
            return@Scaffold
        }
    }

}

@Composable
fun EmptyTransactionsLayout(
    outstandingBalanceAmount: Double,
    repaymentAmount: Double,
    navigateToWalletScreen: () -> Unit,
) {
    val formatter = remember {
        DecimalFormat("0.00")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(56.dp)
            ) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = navigateToWalletScreen
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = Icons.Outlined.AccountBalanceWallet,
                        contentDescription = "button_wallet",
                        tint = MaterialTheme.colors.onBackground
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                SummaryItem(
                    label = "Outstanding Balance",
                    value = formatter.format(outstandingBalanceAmount)
                )
                SummaryItem(
                    label = "Repayment Amount",
                    value = formatter.format(repaymentAmount)
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
