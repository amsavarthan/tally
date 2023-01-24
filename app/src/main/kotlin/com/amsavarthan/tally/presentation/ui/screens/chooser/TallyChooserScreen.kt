package com.amsavarthan.tally.presentation.ui.screens.chooser

import android.os.Parcelable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amsavarthan.tally.domain.entity.*
import com.amsavarthan.tally.presentation.ui.components.TallyAppBar
import com.amsavarthan.tally.presentation.ui.screens.destinations.TallyManageAccountScreenDestination
import com.amsavarthan.tally.presentation.ui.screens.destinations.TallyManageCategoryScreenDestination
import com.amsavarthan.tally.presentation.ui.theme.LightGray
import com.amsavarthan.tally.presentation.utils.ContentDescription
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import kotlinx.parcelize.Parcelize


data class TallyChooserScreenNavArgs(
    val type: ChooserType = ChooserType.Account,
    val selectedId: Long?,
)

@Parcelize
data class TallyChooserResult(
    val accountId: Long? = null,
    val categoryId: Long? = null,
) : Parcelable

@OptIn(ExperimentalFoundationApi::class)
@Destination(navArgsDelegate = TallyChooserScreenNavArgs::class)
@Composable
fun TallyChooserScreen(
    navigator: DestinationsNavigator,
    resultNavigator: ResultBackNavigator<TallyChooserResult>,
    viewModel: ChooserScreenViewModel = hiltViewModel(),
) {

    val uiState = viewModel.uiState
    val (chooserType, selectedId) = viewModel.navArgs

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            TallyAppBar(
                elevation = 0.dp,
                navigationIcon = {
                    IconButton(onClick = resultNavigator::navigateBack) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = ContentDescription.buttonBack
                        )
                    }
                }
            )
        },
    ) { padding ->
        LazyColumn {
            when (chooserType) {
                ChooserType.Account -> {
                    //We don't need Cash Type which is the first element in the list
                    AccountType.getAllAccountTypes().drop(1).forEach { accountType ->
                        ChooserContent(
                            title = accountType.title,
                            selectedId = selectedId,
                            items = uiState.accounts.filter { it.type == accountType },
                            onAddButtonClicked = {
                                navigator.navigate(
                                    TallyManageAccountScreenDestination(
                                        accountType = accountType,
                                    )
                                ) {
                                    launchSingleTop = true
                                }
                            },
                            onItemClicked = { account ->
                                resultNavigator.navigateBack(TallyChooserResult(accountId = account.id))
                            },
                            onItemLongClicked = { account ->
                                navigator.navigate(
                                    TallyManageAccountScreenDestination(
                                        accountId = account.id,
                                        accountType = accountType,
                                    )
                                ) {
                                    launchSingleTop = true
                                }
                            })
                    }
                    if (uiState.cashAccount != null) {
                        item {
                            Column {
                                ChooserTitleItem(name = "Other")
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .combinedClickable(
                                            onClick = {
                                                resultNavigator.navigateBack(
                                                    TallyChooserResult(
                                                        accountId = uiState.cashAccount.id
                                                    )
                                                )
                                            },
                                            onLongClick = {
                                                navigator.navigate(
                                                    TallyManageAccountScreenDestination(
                                                        accountType = AccountType.Cash,
                                                    )
                                                ) {
                                                    launchSingleTop = true
                                                }
                                            })
                                        .background(if (selectedId == uiState.cashAccount.id) LightGray else Color.White)
                                        .padding(vertical = 16.dp, horizontal = 24.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = uiState.cashAccount.name)
                                    Text(text = "₹${uiState.cashAccount.balance}")
                                }
                                Divider(color = LightGray)
                            }
                        }
                    }
                }
                ChooserType.Category -> {
                    CategoryType.getAllCategoryTypes().forEach { categoryType ->
                        ChooserContent(
                            title = categoryType.title,
                            selectedId = selectedId,
                            items = uiState.categories.filter { it.type == categoryType },
                            onAddButtonClicked = {
                                navigator.navigate(
                                    TallyManageCategoryScreenDestination(
                                        categoryType = categoryType,
                                    )
                                ) {
                                    launchSingleTop = true
                                }
                            },
                            onItemClicked = { category ->
                                viewModel.updateAppPreferenceWith(category.id)
                                resultNavigator.navigateBack(TallyChooserResult(categoryId = category.id))
                            },
                            onItemLongClicked = { category ->
                                navigator.navigate(
                                    TallyManageCategoryScreenDestination(
                                        categoryId = category.id,
                                        categoryType = category.type,
                                    )
                                ) {
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
private inline fun <T> LazyListScope.ChooserContent(
    title: String,
    items: List<T>,
    selectedId: Long?,
    crossinline onAddButtonClicked: () -> Unit,
    crossinline onItemClicked: (T) -> Unit,
    crossinline onItemLongClicked: ((T) -> Unit) = {},
) {
    stickyHeader {
        ChooserTitleItem(name = title, onAddButtonClicked = { onAddButtonClicked() })
        Divider(color = LightGray)
    }
    items(items) { item ->
        when (item) {
            is Category -> {
                ChooserItem(
                    name = buildAnnotatedString {
                        withStyle(SpanStyle(fontSize = 20.sp)) {
                            append(item.emoji)
                        }
                        append("  ")
                        append(item.name)
                    },
                    isSelected = selectedId == item.id,
                    onClicked = { onItemClicked(item) },
                    onLongClicked = { onItemLongClicked(item) }
                )
            }
            is Account -> {
                ChooserItem(
                    name = AnnotatedString(item.name),
                    isSelected = selectedId == item.id,
                    onClicked = { onItemClicked(item) },
                    onLongClicked = { onItemLongClicked(item) }
                )
            }
        }
        Divider(color = LightGray)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ChooserItem(
    name: AnnotatedString,
    isSelected: Boolean,
    onClicked: (() -> Unit)? = null,
    onLongClicked: (() -> Unit)? = null,
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                enabled = onClicked != null,
                onClick = onClicked ?: {},
                onLongClick = onLongClicked ?: {},
            )
            .background(if (isSelected) LightGray else Color.White)
            .padding(vertical = 16.dp, horizontal = 24.dp),
        text = name
    )
}

@Composable
private fun ChooserTitleItem(
    name: String,
    onAddButtonClicked: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(
                vertical = if (onAddButtonClicked == null) 24.dp else 8.dp, horizontal = 24.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name, fontWeight = FontWeight.Medium
        )
        if (onAddButtonClicked != null) {
            IconButton(onClick = onAddButtonClicked) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription =ContentDescription.buttonAdd
                )
            }
        }
    }
}
