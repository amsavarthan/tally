package com.amsavarthan.tally.presentation.ui.screens.transactions

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amsavarthan.tally.domain.utils.DateFormatter
import com.amsavarthan.tally.presentation.ui.components.TallyAppBar
import com.amsavarthan.tally.presentation.ui.components.TransactionItem
import com.amsavarthan.tally.presentation.ui.screens.destinations.TallyManageTransactionScreenDestination
import com.amsavarthan.tally.presentation.ui.theme.DarkGray
import com.amsavarthan.tally.presentation.ui.theme.Gray
import com.amsavarthan.tally.presentation.utils.ContentDescription
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalFoundationApi::class)
@Destination
@Composable
fun TallyTransactionsScreen(
    navigator: DestinationsNavigator,
    viewModel: TransactionsScreenViewModel = hiltViewModel(),
) {

    val (transactions, isSearchBarVisible, searchQuery) = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                TransactionsScreenViewModel.UiEvent.NavigateBack -> navigator.navigateUp()
            }
        }
    }

    val lazyListState = rememberLazyListState()
    val scrollOffset = remember {
        derivedStateOf { lazyListState.firstVisibleItemScrollOffset }
    }
    val elevation by animateDpAsState(targetValue = if (scrollOffset.value > 10) AppBarDefaults.TopAppBarElevation else 0.dp)

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            Box {
                TallyAppBar(
                    elevation = elevation,
                    navigationIcon = {
                        IconButton(onClick = {
                            if (isSearchBarVisible) {
                                viewModel.onEvent(TallyTransactionsScreenEvent.ToggleSearchBarVisibility)
                                return@IconButton
                            }
                            navigator.navigateUp()
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = ContentDescription.buttonBack
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            enabled = !isSearchBarVisible,
                            onClick = { viewModel.onEvent(TallyTransactionsScreenEvent.ToggleSearchBarVisibility) },
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = ContentDescription.buttonSearch,
                                tint = Color.Black
                            )
                        }
                    },
                    title = "All Transactions",
                )
                if (isSearchBarVisible) {
                    SearchBarLayout(
                        searchQuery = searchQuery,
                        onSearchQueryChanged = { query ->
                            viewModel.onEvent(
                                TallyTransactionsScreenEvent.OnSearchQueryChanged(
                                    query = query
                                )
                            )
                        }
                    )
                }
            }
        },
    ) { padding ->

        Box(
            modifier = Modifier.padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (transactions.isEmpty() && searchQuery.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .imePadding(),
                    verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No transactions",
                        style = MaterialTheme.typography.h6,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Sorry, there are no results for this search, Please try another phrase.",
                        style = MaterialTheme.typography.subtitle1,
                        color = DarkGray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            LazyColumn {
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

@Composable
fun BoxScope.SearchBarLayout(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
) {
    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Row(
        modifier = Modifier
            .align(Alignment.CenterEnd)
            .fillMaxWidth(0.86f)
            .background(Color.White)
            .padding(end = 16.dp, top = 16.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BasicTextField(
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester)
                .padding(start = 8.dp),
            value = searchQuery,
            textStyle = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Normal),
            onValueChange = onSearchQueryChanged,
            singleLine = true,
            decorationBox = { innerTextField ->
                Box {
                    if (searchQuery.isBlank()) {
                        Text(
                            text = "Search...",
                            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Normal),
                            color = Gray
                        )
                    }
                    innerTextField()
                }
            }
        )
        AnimatedVisibility(
            visible = searchQuery.isNotBlank(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            IconButton(
                modifier = Modifier.padding(start = 8.dp),
                onClick = { onSearchQueryChanged("") },
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = ContentDescription.buttonClose,
                    tint = Color.Black
                )
            }
        }
    }
}
