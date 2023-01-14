package com.amsavarthan.tally.presentation.ui.screens.accounts

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.twotone.AccountBalanceWallet
import androidx.compose.material.icons.twotone.CreditCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amsavarthan.tally.domain.entity.AccountType
import com.amsavarthan.tally.presentation.ui.components.AccountItem
import com.amsavarthan.tally.presentation.ui.components.TallyAppBar
import com.amsavarthan.tally.presentation.ui.screens.destinations.TallyManageAccountScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


data class TallyAccountsScreenNavArgs(
    val accountType: AccountType,
)

const val suffixForPlural = "s"

@Destination(navArgsDelegate = TallyAccountsScreenNavArgs::class)
@Composable
fun TallyAccountsScreen(
    navigator: DestinationsNavigator,
    viewModel: AccountsViewModel = hiltViewModel(),
) {

    val (accounts) = viewModel.uiState
    val accountType = viewModel.accountType

    val lazyListState = rememberLazyListState()
    val hasListScrolled = remember {
        derivedStateOf { lazyListState.firstVisibleItemScrollOffset > 16 }
    }

    val icon = remember(accountType) {
        when (accountType) {
            AccountType.CreditCard, AccountType.DebitCard -> Icons.TwoTone.CreditCard
            AccountType.Cash, AccountType.PayLater -> Icons.TwoTone.AccountBalanceWallet
        }
    }

    val elevation by animateDpAsState(targetValue = if (hasListScrolled.value) AppBarDefaults.TopAppBarElevation else 0.dp)

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            TallyAppBar(
                elevation = elevation,
                navigationIcon = {
                    IconButton(onClick = navigator::navigateUp) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "button_back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            state = lazyListState,
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 24.dp,
                top = 12.dp
            )
        ) {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Icon(
                        modifier = Modifier.size(42.dp),
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onBackground,
                    )
                    Text(
                        text = "${accountType.title}$suffixForPlural",
                        color = MaterialTheme.colors.onBackground,
                        style = MaterialTheme.typography.h4,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = "Manage all your ${accountType.title.lowercase()}$suffixForPlural here.",
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Normal,
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(accounts) { account ->
                Spacer(modifier = Modifier.height(16.dp))
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
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                AccountItem(
                    name = if (accountType == AccountType.PayLater) "Add account" else "Add card",
                    onClick = {
                        navigator.navigate(
                            TallyManageAccountScreenDestination(
                                accountType = accountType,
                            )
                        ) {
                            launchSingleTop = true
                        }
                    },
                    filled = true
                )
            }
        }
    }
}